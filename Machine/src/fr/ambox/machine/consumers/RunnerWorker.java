package fr.ambox.machine.consumers;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;

public class RunnerWorker extends Worker {

    public RunnerWorker() {
        super(Worker.Q_PROGRAM);
    }

    @Override
    DefaultConsumer createConsumer(Channel channel) {
        return new RunnerConsumer(channel);
    }
}
