package fr.ambox.machine.consumers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import fr.ambox.assembler.exceptions.TokenParsingException;
import fr.ambox.compiler.exceptions.MultipleParseTreeException;
import fr.ambox.compiler.exceptions.SyntaxErrorException;
import fr.ambox.lexer.NoTokenException;
import fr.ambox.machine.consumers.models.Job;
import fr.ambox.machine.consumers.models.Report;
import fr.ambox.machine.consumers.models.Task;
import fr.ambox.program.Program;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Logger;

public class MakerConsumer extends LangConsumer {
    private static final Logger LOGGER = Logger.getLogger(LangConsumer.class.getName());

    public MakerConsumer(Channel channel) {
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
        Job job = null;
        try {
            Gson gson = new Gson();
            job = gson.fromJson(message, Job.class);
            if (job == null) {
                throw new Exception("No job");
            }
            if (job.code == null) {
                throw new Exception("No code");
            }
        }
        catch (JsonSyntaxException e1) {
            LOGGER.severe("JSON syntax error");
            return;
        }
        catch (Exception e2) {
            LOGGER.severe("Incomplete job");
            return;
        }

        try {
            try {
                long start = System.currentTimeMillis();
                Program program = Program.make(job.code);
                long end = System.currentTimeMillis();

                if (job.shouldLaunch()) {
                    program.getContext().getMeta().jobId = job.id;
                    Task task = new Task(program);
                    this.publishTask(task);
                }

                Report report = new Report(job);
                report.status = "up";
                if (!job.shouldLaunch()) {
                    report.status = "down";
                }
                report.programState = program.getState().name();
                report.processingTime = (end - start);
                this.publishReport(report);
            }
            catch (SyntaxErrorException | NoTokenException | MultipleParseTreeException | TokenParsingException e) {
                Report report = new Report(job);
                report.status = "down";
                report.stdout = e.getMessage();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                PrintStream out = new PrintStream(buffer);
                e.printStackTrace(out);
                report.stderr = new String(buffer.toByteArray(), "UTF-8");
                this.publishReport(report);
            }
        }
        catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }
}