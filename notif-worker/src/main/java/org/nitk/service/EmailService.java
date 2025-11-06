package org.nitk.service;

import org.nitk.common.dto.AlertDTO;
import org.nitk.common.dto.AlertNotificationDTO;
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
                .to("some user", notif.getEmail())
                .withSubject("Alert : " + alert.getSeverity())
                .withPlainText("This is to notify you about alert: " + alert.getMessage() +
                        "\nPlease take necessary actions.\n\nRegards,\nAlert System")
                .buildEmail();

        mailer.sendMail(email);
    }
}
