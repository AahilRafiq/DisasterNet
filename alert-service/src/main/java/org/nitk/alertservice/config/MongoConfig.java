package org.nitk.alertservice.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Bean
    public MongoClient mongoClient() {
        String mongoHost = System.getenv("MONGO_HOST");
        if (mongoHost == null || mongoHost.isEmpty()) {
            mongoHost = "localhost";
        }
        return MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString("mongodb://" + mongoHost + ":27017"))
                        .build()
        );
    }
}
