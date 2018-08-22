package fr.ambox.machine.consumers;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;

public class SchedulerWorker extends Worker {
    public SchedulerWorker() {
        super(Worker.Q_TASK);
    }

    @Override
    DefaultConsumer createConsumer(Channel channel) {
        return new SchedulerConsumer(channel);
    }
}
