package org.nitk.alertservice.controller;

import org.nitk.alertservice.dto.AlertDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class AlertController {
    @GetMapping("/alerts/ping")
    public String ping() {
        return "Alert Service is up and running!";
    }

    @PostMapping("/alerts/new")
    public String createAlert(@RequestBody AlertDTO alert) {
        // Todo: Save alert to database and notify users within the specified distance
        System.out.println("Received alert: " + alert.getMessage());
        System.out.println("Severity: " + alert.getSeverity());

        return "Alert created successfully!";
    }
}
