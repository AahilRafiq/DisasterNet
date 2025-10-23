package org.nitk.requestservice.controller;

import org.nitk.requestservice.service.RequestService;
import org.nitk.common.dto.ResourceRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    // Create a new request
    @PostMapping
    public ResponseEntity<ResourceRequestDTO> createRequest(@RequestBody ResourceRequestDTO request) {
        ResourceRequestDTO created = requestService.createRequest(request);
        return ResponseEntity.status(201).body(created);
    }

    // Get request by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResourceRequestDTO> getRequest(@PathVariable String id) {
        ResourceRequestDTO request = requestService.getRequestById(id);
        if (request == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(request);
    }

    // List open requests
    @GetMapping("/open")
    public ResponseEntity<List<ResourceRequestDTO>> getOpenRequests() {
        List<ResourceRequestDTO> openRequests = requestService.getOpenRequests();
        return ResponseEntity.ok(openRequests);
    }

    // Assign volunteer
    @PatchMapping("/{id}/assign")
    public ResponseEntity<String> assignVolunteer(@PathVariable String id, @RequestParam Long volunteerId) {
        // Only admin / volunteer can assign, check using cookie here



        boolean success = requestService.assignVolunteer(id, volunteerId);
        if (!success) return ResponseEntity.badRequest().body("Cannot assign volunteer");
        return ResponseEntity.ok("Volunteer assigned successfully");
    }

    // Get nearby open requests for volunteer/Admin
    @GetMapping("/nearby")
    public ResponseEntity<List<ResourceRequestDTO>> getNearbyRequests(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam( required = false, defaultValue = "5000") Long maxDistance // in meters
    ) {
        List<ResourceRequestDTO> nearbyRequests = requestService.getNearbyOpenRequests(latitude, longitude, maxDistance);
        return ResponseEntity.ok(nearbyRequests);
    }
}
