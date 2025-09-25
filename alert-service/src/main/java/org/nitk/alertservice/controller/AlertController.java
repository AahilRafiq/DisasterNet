package org.nitk.alertservice.controller;

import org.nitk.alertservice.service.AlertService;
import org.nitk.common.dto.AlertDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class AlertController {

    private final AlertService alertService;

    @Autowired
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/alerts/ping")
    public String ping() {
        return "Alert Service is up and running!";
    }

    @PostMapping("/alerts/new")
    public String createAlert(@RequestBody AlertDTO alert) {
        System.out.println("Received alert: " + alert.getMessage());
        System.out.println("Severity: " + alert.getSeverity());

        final String finalAlertId = alertService.insertAlert(alert);
        if(finalAlertId != null) {
            alertService.publishNotifications(finalAlertId, alert);
        }

        return "Alert created successfully!";
    }

}
