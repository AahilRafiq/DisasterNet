package org.nitk.helpers;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.nitk.common.mongo.MongoCollections;
import org.nitk.service.AlertService;
import org.nitk.service.EmailService;

public class AppContext {
    private static AppContext instance = null;

    public static AppContext getInstance() {
        if(instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    private AppContext() {
        System.out.println("AppContext initialized");
    }

    private final MongoCollections mongoCollections = new MongoCollections(createMongoClient());
    public MongoCollections getMongoCollections() {
        return mongoCollections;
    }

    private final AlertService alertService = new AlertService();
    public AlertService getAlertService() {
        return alertService;
    }

    private final EmailService emailService = new EmailService();
    public EmailService getEmailService() {
        return emailService;
    }

    // Helper methods...........................
    private static MongoClient createMongoClient() {
        String mongoHost = System.getenv("MONGO_HOST");
        if (mongoHost == null || mongoHost.isEmpty()) {
            mongoHost = "localhost";
        }
        return MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString("mongodb://"+mongoHost+":27017"))
                        .build()
        );
    }
}
