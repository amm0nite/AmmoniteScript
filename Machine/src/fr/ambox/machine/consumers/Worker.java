package fr.ambox.machine.consumers;

import com.rabbitmq.client.*;
import fr.ambox.machine.Config;
import fr.ambox.machine.Env;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

abstract class Worker implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(Worker.class.getName());

    public static final String Q_JOB = "playground_job";
    public static final String Q_TASK = "playground_task";
    public static final String Q_REPORT = "playground_report";
    public static final String Q_PROGRAM = "playground_program";

    private String queueName;

    public Worker(String queueName) {
        this.queueName = queueName;
    }

    public void run() {
        try {
            Config cnf = new Config();
            LOGGER.info("Connecting to " + cnf.getHost());
            ConnectionFactory factory = new ConnectionFactory();
            factory.setAutomaticRecoveryEnabled(true);
            factory.setHost(cnf.getHost());
            factory.setUsername(cnf.getUsername());
            factory.setPassword(cnf.getPassword());

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            LOGGER.info("Connected");

            channel.queueDeclare(this.queueName, true, false, false, null);
            channel.basicQos(1);

            DefaultConsumer consumer = this.createConsumer(channel);
            channel.basicConsume(this.queueName, false, consumer);
        }
        catch (IOException | TimeoutException e) {
            LOGGER.severe("Network error: " + e.getMessage());
            try {
                LOGGER.info("Waiting (error)");
                Thread.sleep(5000);
                LOGGER.info("Restarting (error)");
                this.run();
            } catch (InterruptedException __ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }

    abstract DefaultConsumer createConsumer(Channel channel);
}
