package fr.ambox.machine.consumers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import fr.ambox.machine.consumers.models.Report;
import fr.ambox.machine.consumers.models.Task;
import fr.ambox.program.Program;
import fr.ambox.program.ProgramState;
import fr.ambox.program.serialization.adapters.ProgramAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class RunnerConsumer extends LangConsumer {
    private static final Logger LOGGER = Logger.getLogger(LangConsumer.class.getName());

    public RunnerConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");

        LOGGER.info("Received");
        this.process(message);
        LOGGER.info("Done");
        this.getChannel().basicAck(envelope.getDeliveryTag(), false);
    }

    private void process(String message) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Program.class, new ProgramAdapter());
        Gson gson = gsonBuilder.create();

        Program program = null;
        try {
            program = gson.fromJson(message, Program.class);
            if (program == null) {
                throw new Exception("No program");
            }
            if (program.getContext().getMeta() == null) {
                throw  new Exception("No metadata");
            }
        }
        catch (JsonSyntaxException e1) {
            LOGGER.severe("JSON syntax error");
            return;
        }
        catch (Exception e2) {
            LOGGER.severe("Incomplete program");
            return;
        }

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayOutputStream err = new ByteArrayOutputStream();
            program.getContext().setStdout(out);
            program.getContext().setStderr(err);

            long start = System.currentTimeMillis();
            program.run();
            long end = System.currentTimeMillis();

            if (program.getState() == ProgramState.Error) {
                err.write(program.getError().getMessage().getBytes("UTF-8"));
            }

            int jobId = program.getContext().getMeta().jobId;
            Report report = new Report(jobId);
            report.stdout = new String(out.toByteArray(), "UTF-8");
            report.stderr = new String(err.toByteArray(), "UTF-8");
            report.status = "up";
            if (program.getState() == ProgramState.Error || program.getState() == ProgramState.Done) {
                report.status = "down";
            }
            report.programState = program.getState().name();
            report.processingTime = (end - start);
            this.publishReport(report);

            if (program.getState() == ProgramState.Paused) {
                Task task = new Task(program);
                this.publishTask(task);
            }
        }
        catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }
}
