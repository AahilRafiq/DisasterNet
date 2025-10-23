package org.nitk.common.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.nitk.common.dto.AlertDTO;
import org.nitk.common.dto.ResourceRequestDTO;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class MongoCollections {
    public final MongoCollection<Document> userCollection;
    public final MongoCollection<AlertDTO> alertCollection;
    public final MongoCollection<ResourceRequestDTO> requestCollection;
    public final String databaseName = "DisasterNet";

    public MongoCollections(MongoClient mongoClient) {
        CodecProvider codecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry codecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(codecProvider));

        MongoDatabase database = mongoClient.getDatabase(databaseName).withCodecRegistry(codecRegistry);

        this.userCollection = database.getCollection("users");
        this.alertCollection = database.getCollection("alerts", AlertDTO.class);
        this.requestCollection = database.getCollection("requests", ResourceRequestDTO.class);

        // Create 2dsphere index for geospatial queries
        this.requestCollection.createIndex(Indexes.geo2dsphere("location"));
    }

    public MongoCollection<Document> getUserCollection() {
        return userCollection;
    }

    public MongoCollection<AlertDTO> getAlertCollection() {
        return alertCollection;
    }

    public MongoCollection<ResourceRequestDTO> getRequestCollection() { return requestCollection; }
}
