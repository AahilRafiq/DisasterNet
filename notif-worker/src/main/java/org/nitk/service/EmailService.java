package org.nitk.service;

import org.nitk.common.dto.AlertDTO;
import org.nitk.common.dto.AlertNotificationDTO;
import org.nitk.common.dto.RequestNotificationDTO;
import org.nitk.common.dto.ResourceRequestDTO;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

public class EmailService {
    private final Mailer mailer;

    public EmailService() {
        String smtpHost = System.getenv("SMTP_HOST");
        if(smtpHost == null || smtpHost.isEmpty()) {
            smtpHost = "localhost";
        }
        this.mailer = MailerBuilder
                .withSMTPServer(smtpHost, 1025)
                .buildMailer();
    }

    public void sendEmail(AlertNotificationDTO notif, AlertDTO alert) {
        Email email = EmailBuilder.startingBlank()
                .from("DisasterNet", "disasternet@server.com")
                .to(notif.getEmail().substring(0,notif.getEmail().indexOf('@')), notif.getEmail())
                .withSubject("Alert : " + alert.getSeverity())
                .withPlainText("This is to notify you about alert: " + alert.getMessage() +
                        "\nPlease take necessary actions.\n\nRegards,\nAlert System")
                .buildEmail();

        mailer.sendMail(email);
    }

    public void sendRequestEmail(RequestNotificationDTO notif, ResourceRequestDTO request) {
        String type = request != null ? request.getType() : "Request";
        String desc = request != null ? request.getDescription() : "";
        Email email = EmailBuilder.startingBlank()
                .from("DisasterNet", "disasternet@server.com")
                .to("Volunteer", notif.getEmail())
                .withSubject("New Resource Request: " + type)
                .withPlainText("A new resource request has been created.\n\nType: " + type +
                        "\nDescription: " + desc +
                        "\n\nPlease check the system to assist.\n\nRegards,\nDisasterNet")
                .buildEmail();
        mailer.sendMail(email);
    }
}
