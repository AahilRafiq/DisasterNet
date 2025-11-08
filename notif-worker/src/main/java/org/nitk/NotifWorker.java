package org.nitk;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import helpers.AppContext;
import org.nitk.common.dto.AlertDTO;
import org.nitk.common.dto.AlertNotificationDTO;
import org.nitk.common.dto.RequestNotificationDTO;
import org.nitk.common.dto.ResourceRequestDTO;
import service.AlertService;
import service.EmailService;
import service.RequestService;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class NotifWorker {
    private final static String ALERTS_QUEUE = "ALERTS";
    private final static String REQUESTS_QUEUE = "REQUESTS";

    public static void main(String[] args) throws IOException, TimeoutException {
        AppContext ctx = AppContext.getInstance();
        AlertService alertService = ctx.getAlertService();
        RequestService requestService = new RequestService();
        EmailService emailService = ctx.getEmailService();
        ObjectMapper mapper = new ObjectMapper();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();

        // Alerts consumer
        Channel alertChannel = connection.createChannel();
        alertChannel.queueDeclare(ALERTS_QUEUE, false, false, false, null);
        System.out.println(" [*] Waiting for ALERTS and REQUESTS messages. To exit press CTRL+C");

        DeliverCallback alertCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            var notif = mapper.readValue(message, AlertNotificationDTO.class);

            AlertDTO alert = alertService.getAlert(notif.getAlertId());
            emailService.sendEmail(notif, alert);

            System.out.println(" [x] Processed ALERT: " + message);
        };
        alertChannel.basicConsume(ALERTS_QUEUE, true, alertCallback, consumerTag -> { });

        // Requests consumer
        Channel reqChannel = connection.createChannel();
        reqChannel.queueDeclare(REQUESTS_QUEUE, false, false, false, null);
        DeliverCallback reqCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            var notif = mapper.readValue(message, RequestNotificationDTO.class);

            ResourceRequestDTO req = requestService.getRequest(notif.getRequestId());
            emailService.sendRequestEmail(notif, req);

            System.out.println(" [x] Processed REQUEST: " + message);
        };
        reqChannel.basicConsume(REQUESTS_QUEUE, true, reqCallback, consumerTag -> { });
    }
}