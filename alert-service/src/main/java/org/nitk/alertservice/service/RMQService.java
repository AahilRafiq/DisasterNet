package org.nitk.alertservice.service;

import org.springframework.stereotype.Service;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

@Service
public class RMQService {
    private final static String QUEUE_NAME = "ALERTS";
    private final ConnectionFactory factory;

    public RMQService() {
        factory = new ConnectionFactory();
        String rmqHost = System.getenv("RABBITMQ_HOST");
        if (rmqHost == null || rmqHost.isEmpty()) {
            rmqHost = "localhost";
        }
        factory.setHost(rmqHost);
    }

    public void publishNotification(byte[] message) {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, message);
            System.out.println(" [x] Sent message to RMQ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
