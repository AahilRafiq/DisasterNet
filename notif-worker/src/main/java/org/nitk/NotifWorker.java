package org.nitk;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import helpers.AppContext;
import org.nitk.common.dto.AlertDTO;
import org.nitk.common.dto.AlertNotificationDTO;
import service.AlertService;
import service.EmailService;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class NotifWorker {
    private final static String QUEUE_NAME = "ALERTS";

    public static void main(String[] args) throws IOException, TimeoutException {
        AppContext ctx = AppContext.getInstance();
        AlertService alertService = ctx.getAlertService();
        EmailService emailService = ctx.getEmailService();
        ObjectMapper mapper = new ObjectMapper();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            var notif = mapper.readValue(message, AlertNotificationDTO.class);

            AlertDTO alert = alertService.getAlert(notif.getAlertId());
            emailService.sendEmail(notif, alert);

            System.out.println(" [x] Sent '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }
}