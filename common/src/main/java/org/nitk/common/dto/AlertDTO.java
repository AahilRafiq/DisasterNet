package org.nitk.common.dto;

public class AlertDTO {
    private String message;
    private String severity;
    private LocationDTO location;
    private Long distance; // in meters

    public AlertDTO() {}

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public LocationDTO getLocation() { return location; }
    public void setLocation(LocationDTO location) { this.location = location; }

    public Long getDistance() { return distance; }
    public void setDistance(Long distance) { this.distance = distance; }
}
