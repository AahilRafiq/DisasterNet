package org.nitk.common.dto;

import org.bson.types.ObjectId;

public class AlertDTO {
    private ObjectId id;
    private String message;
    private String severity;
    private LocationDTO location;
    private Long distance; // in meters

    public AlertDTO() {}

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public LocationDTO getLocation() { return location; }
    public void setLocation(LocationDTO location) { this.location = location; }

    public Long getDistance() { return distance; }
    public void setDistance(Long distance) { this.distance = distance; }
}
