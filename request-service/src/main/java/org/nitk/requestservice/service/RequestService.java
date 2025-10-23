package org.nitk.requestservice.service;

import com.mongodb.client.result.InsertOneResult;
import org.bson.types.ObjectId;
import org.nitk.common.dto.ResourceRequestDTO;
import org.nitk.common.mongo.MongoCollections;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Service
public class RequestService {

    private final MongoCollections mongoCollections;

    public RequestService(MongoCollections mongoCollections) {
        this.mongoCollections = mongoCollections;
    }

    // Create a new resource request
    public ResourceRequestDTO createRequest(ResourceRequestDTO request) {
        request.setCreatedAt(Instant.now());
        request.setUpdatedAt(Instant.now());
        request.setStatus("pending");
        request.setId(null); // Always set to null to let MongoDB generate ObjectId

        // Insert into MongoDB
        InsertOneResult result = mongoCollections.getRequestCollection().insertOne(request);
        if (result.getInsertedId() != null && result.getInsertedId().isObjectId()) {
            request.setId(result.getInsertedId().asObjectId().getValue());
        }
        return request;
    }

    // Fetch request by ID
    public ResourceRequestDTO getRequestById(String id) {
        ObjectId objId = new ObjectId(id);
        return mongoCollections.getRequestCollection().find(eq("_id", objId)).first();
    }

    // List all open requests
    public List<ResourceRequestDTO> getOpenRequests() {
        List<ResourceRequestDTO> openRequests = new ArrayList<>();
        mongoCollections.getRequestCollection().find(eq("status", "pending"))
                .into(openRequests);
        return openRequests;
    }

    // Assign a volunteer
    public boolean assignVolunteer(String requestId, Long volunteerId) {



        // Only volunteer can be assigned

        // Only assign if request is still pending

        ObjectId objId = new ObjectId(requestId);
        ResourceRequestDTO request = mongoCollections.getRequestCollection().find(eq("_id", objId)).first();
        if (request == null || !"pending".equals(request.getStatus())) {
            return false; // cannot assign
        }

        request.setAssignedVolunteerId(volunteerId);
        request.setStatus("assigned");
        request.setUpdatedAt(Instant.now());

        mongoCollections.getRequestCollection().replaceOne(eq("_id", objId), request);
        return true;
    }

    // Get nearby open requests for volunteer/Admin
    public List<ResourceRequestDTO> getNearbyOpenRequests(Double latitude, Double longitude, Long maxDistance) {
        List<ResourceRequestDTO> nearbyRequests = new ArrayList<>();
        mongoCollections.getRequestCollection().find(
                new org.bson.Document("location",
                        new org.bson.Document("$near",
                                new org.bson.Document("$geometry",
                                        new org.bson.Document()
                                                .append("type", "Point")
                                                .append("coordinates", List.of(longitude, latitude))
                                )
                                        .append("$maxDistance", maxDistance)
                        )
                ).append("status", "pending")
        ).into(nearbyRequests);
        return nearbyRequests;
    }
}
