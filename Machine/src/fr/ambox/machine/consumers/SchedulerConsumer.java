package fr.ambox.machine.consumers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import fr.ambox.assembler.MetaData;
import fr.ambox.machine.consumers.models.Report;
import fr.ambox.machine.consumers.models.Task;
import fr.ambox.program.Program;
import fr.ambox.program.serialization.adapters.ProgramAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class SchedulerConsumer extends LangConsumer {
    private static final Logger LOGGER = Logger.getLogger(LangConsumer.class.getName());

    private ArrayList<Integer> stoppedPrograms;

    public SchedulerConsumer(Channel channel) {
        super(channel);
        this.stoppedPrograms = new ArrayList<Integer>();
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

        Task task = null;
        try {
            task = gson.fromJson(message, Task.class);
            if (task == null) {
                throw new Exception("No task");
            }
        }
        catch (JsonSyntaxException e1) {
            LOGGER.severe("JSON syntax error");
            return;
        }
        catch (Exception e2) {
            LOGGER.severe("Incomplete task");
            return;
        }

        try {
            if (task.type.equals("program")) {
                this.processProgramTask(task.program);
            }
            if (task.type.equals("stop")) {
                this.processStopTask(task.stopJobId);
            }
        }
        catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    private void processStopTask(int stopJobId) {
        this.stoppedPrograms.add(stopJobId);
    }

    private void processProgramTask(Program program) throws IOException {
        MetaData meta = program.getContext().getMeta();
        if (meta == null) {
            return;
        }
        if (this.stoppedPrograms.contains(meta.jobId)) {
            this.stoppedPrograms.remove(Integer.valueOf(meta.jobId));
            Report report = new Report(meta.jobId);
            report.status = "down";
            report.programState = program.getState().name();
            this.publishReport(report);
            return;
        }
        this.publishProgram(program);
    }
}
