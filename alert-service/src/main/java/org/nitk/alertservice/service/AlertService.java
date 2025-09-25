package org.nitk.alertservice.service;

import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.nitk.common.dto.AlertDTO;
import org.nitk.common.dto.AlertNotificationDTO;
import org.nitk.common.mongo.MongoCollections;
import org.springframework.stereotype.Service;

@Service
public class AlertService {
    private final MongoCollections mongoCollections;

    public AlertService(MongoCollections mongoCollections) {
        this.mongoCollections = mongoCollections;
    }

    public String insertAlert(AlertDTO alert) {
        InsertOneResult insertResult = mongoCollections.getAlertCollection().insertOne(alert);
        String alertId = null;
        if (insertResult.getInsertedId() != null) {
            var inserted = insertResult.getInsertedId();
            if (inserted.isObjectId()) {
                alertId = inserted.asObjectId().getValue().toHexString();
            } else {
                alertId = inserted.toString();
            }
        }

        return alertId;
    }

    public void publishNotifications(String alertId, AlertDTO alert) {
        var results = mongoCollections.getUserCollection().find(
                new Document("location",
                        new Document("$near",
                                new Document("$geometry",
                                        new Document()
                                                .append("type", "Point")
                                                .append("coordinates", alert.getLocation().getCoordinates())
                                )
                                        .append("$maxDistance", alert.getDistance())
                        )
                )
        );

        results.forEach(doc -> {
            Number userIdNum = doc.get("userId", Number.class);
            Long userId = userIdNum != null ? userIdNum.longValue() : null;

            AlertNotificationDTO dto = new AlertNotificationDTO();
            dto.setUserId(userId);
            dto.setEmail(doc.getString("email"));
            dto.setPhone(doc.getString("phone"));
            dto.setRole(doc.getString("role"));
            dto.setAlertId(alertId);

            System.out.println("Prepared notification DTO: " + dto.getUserId() + ", " + dto.getEmail() + ", " + dto.getPhone() + ", " + dto.getRole() + ", " + dto.getAlertId());
        });

        // Todo: Publish to RabbitMQ
    }
}
