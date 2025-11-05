package org.nitk.common.dto;

import org.bson.types.ObjectId;
import java.time.Instant;

public class ResourceRequestDTO {
    private ObjectId id;
    private Long citizenId;
    private String type; // food | water | shelter | medical
    private String description;
    private LocationDTO location; // reuse your LocationDTO
    private String status; // pending | assigned | completed
    private Long assignedVolunteerId;
    private Instant createdAt;
    private Instant updatedAt;

    public ResourceRequestDTO() {
        this.status = "pending";
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public Long getCitizenId() { return citizenId; }
    public void setCitizenId(Long citizenId) { this.citizenId = citizenId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocationDTO getLocation() { return location; }
    public void setLocation(LocationDTO location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getAssignedVolunteerId() { return assignedVolunteerId; }
    public void setAssignedVolunteerId(Long assignedVolunteerId) { this.assignedVolunteerId = assignedVolunteerId; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
