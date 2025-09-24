package org.nitk.common.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;


public class MongoCollections {
    public final MongoCollection<Document> userCollection;
    public final String databaseName = "DisasterNet";

    public MongoCollections(MongoClient mongoClient) {
        this.userCollection = mongoClient.getDatabase(databaseName).getCollection("users");
    }

    public MongoCollection<Document> getUserCollection() {
        return userCollection;
    }
}
