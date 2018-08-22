package fr.ambox.machine.consumers;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;

public class MakerWorker extends Worker {

    public MakerWorker() {
        super(Worker.Q_JOB);
    }

    @Override
    DefaultConsumer createConsumer(Channel channel) {
        return new MakerConsumer(channel);
    }
}
