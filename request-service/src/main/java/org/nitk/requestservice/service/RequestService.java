package org.nitk.requestservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.nitk.common.dto.ResourceRequestDTO;
import org.nitk.common.dto.RequestNotificationDTO;
import org.nitk.common.dto.ResourceRequestPlainDTO;
import org.nitk.common.mongo.MongoCollections;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Service
public class RequestService {

    private final MongoCollections mongoCollections;
    private final RMQService rmqService;
    private final ObjectMapper mapper = new ObjectMapper();

    public RequestService(MongoCollections mongoCollections, RMQService rmqService) {
        this.mongoCollections = mongoCollections;
        this.rmqService = rmqService;
    }

    // Create a new resource request
    public ResourceRequestDTO createRequest(ResourceRequestDTO request) {
        request.setCreatedAt(Instant.now());
        request.setCitizenId(request.getCitizenId());
        request.setUpdatedAt(Instant.now());
        request.setStatus("pending");
        request.setId(null); // Always set to null to let MongoDB generate ObjectId

        // Insert into MongoDB
        InsertOneResult result = mongoCollections.getRequestCollection().insertOne(request);
        String requestId = null;
        if (result.getInsertedId() != null && result.getInsertedId().isObjectId()) {
            request.setId(result.getInsertedId().asObjectId().getValue());
            requestId = result.getInsertedId().asObjectId().getValue().toHexString();
        }

        // Publish notifications to all volunteers
        if (requestId != null) {
            publishNotificationsToVolunteers(requestId);
        }
        return request;
    }

    private void publishNotificationsToVolunteers(String requestId) {
        var volunteers = mongoCollections.getUserCollection().find(
                new Document("role", new Document("$in", List.of("volunteer", "VOLUNTEER")))
        );
        volunteers.forEach(doc -> {
            Number userIdNum = doc.get("userId", Number.class);
            Long userId = userIdNum != null ? userIdNum.longValue() : null;

            RequestNotificationDTO dto = new RequestNotificationDTO();
            dto.setUserId(userId);
            dto.setEmail(doc.getString("email"));
            dto.setPhone(doc.getString("phone"));
            dto.setRole(doc.getString("role"));
            dto.setRequestId(requestId);
            try {
                byte[] messageData = mapper.writeValueAsBytes(dto);
                rmqService.publishNotification(messageData);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // Fetch request by ID
    public ResourceRequestDTO getRequestById(String id) {
        ObjectId objId = new ObjectId(id);
        return mongoCollections.getRequestCollection().find(eq("_id", objId)).first();
    }

    // List all open requests
    public List<ResourceRequestPlainDTO> getOpenRequests() {
        List<ResourceRequestDTO> openRequests = new ArrayList<>();
        mongoCollections.getRequestCollection().find(eq("status", "pending"))
                .into(openRequests);
        List<ResourceRequestPlainDTO> plainDTOS = new ArrayList<>();
        for (ResourceRequestDTO dto : openRequests) {
            ResourceRequestPlainDTO plainDTO = new ResourceRequestPlainDTO();
            plainDTO.setId(dto.getId());
            plainDTO.setCitizenId(dto.getCitizenId());
            plainDTO.setType(dto.getType());
            plainDTO.setDescription(dto.getDescription());
            plainDTO.setLocation(dto.getLocation());
            plainDTO.setStatus(dto.getStatus());
            plainDTO.setAssignedVolunteerId(dto.getAssignedVolunteerId());
            plainDTO.setCreatedAt(dto.getCreatedAt());
            plainDTO.setUpdatedAt(dto.getUpdatedAt());
            plainDTOS.add(plainDTO);
        }
        return plainDTOS;
    }

    // Assign a volunteer
    public boolean assignVolunteer(String requestId, Long volunteerId) {



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
