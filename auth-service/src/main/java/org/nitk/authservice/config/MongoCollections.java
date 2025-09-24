package org.nitk.authservice.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MongoCollections {
    public final MongoCollection<Document> userCollection;
    public final String databaseName = "DisasterNet";

    public MongoCollections(@Autowired MongoClient mongoClient) {
        this.userCollection = mongoClient.getDatabase(databaseName).getCollection("users");
    }

    public MongoCollection<Document> getUserCollection() {
        return userCollection;
    }
}
