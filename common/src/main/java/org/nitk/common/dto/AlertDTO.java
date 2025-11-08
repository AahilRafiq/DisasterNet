package org.nitk.common.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;

import java.time.Instant;

public class AlertDTO {
    private ObjectId id;
    private String message;
    private String severity;
    private LocationDTO location;
    private Long distance; // in meters
    private Instant createdAt;

    public AlertDTO() {}

    @JsonIgnore
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    @JsonGetter("id")
    public String getIdAsString() {
        return id != null ? id.toHexString() : null;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public LocationDTO getLocation() { return location; }
    public void setLocation(LocationDTO location) { this.location = location; }

    public Long getDistance() { return distance; }
    public void setDistance(Long distance) { this.distance = distance; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
