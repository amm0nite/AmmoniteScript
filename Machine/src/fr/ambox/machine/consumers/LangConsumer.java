package fr.ambox.machine.consumers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import fr.ambox.machine.consumers.models.Report;
import fr.ambox.machine.consumers.models.Task;
import fr.ambox.program.Program;
import fr.ambox.program.serialization.adapters.ProgramAdapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

abstract class LangConsumer extends DefaultConsumer {
    private static final Logger LOGGER = Logger.getLogger(LangConsumer.class.getName());

    public LangConsumer(Channel channel) {
        super(channel);
    }

    protected void publishReport(Report report) throws IOException {
        Gson gson = new Gson();
        byte[] messageBytes = gson.toJson(report).getBytes("UTF-8");
        String name = Worker.Q_REPORT;
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("x-message-ttl", 600000);
        this.getChannel().queueDeclare(name, true, false, false, args);
        this.getChannel().basicPublish("", name, null, messageBytes);
        LOGGER.info("Publish report");
    }

    protected void publishProgram(Program program) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Program.class, new ProgramAdapter());
        Gson gson = gsonBuilder.create();
        byte[] messageBytes = gson.toJson(program, Program.class).getBytes("UTF-8");
        String name = Worker.Q_PROGRAM;
        HashMap<String, Object> args = new HashMap<String, Object>();
        this.getChannel().queueDeclare(name, true, false, false, args);
        this.getChannel().basicPublish("", name, null, messageBytes);
        LOGGER.info("Publish program");
    }

    protected void publishTask(Task task) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Program.class, new ProgramAdapter());
        Gson gson = gsonBuilder.create();
        byte[] messageBytes = gson.toJson(task).getBytes("UTF-8");
        String name = Worker.Q_TASK;
        HashMap<String, Object> args = new HashMap<String, Object>();
        this.getChannel().queueDeclare(name, true, false, false, args);
        this.getChannel().basicPublish("", name, null, messageBytes);
        LOGGER.info("Publish task");
    }
}
