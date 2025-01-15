package com.shpigel.vesselmanager01;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vessels")
public class VesselController {

    private final VesselService vesselService;

    public VesselController(VesselService vesselService) {
        this.vesselService = vesselService;
    }

    @PostMapping
    public ResponseEntity<Vessel> createVessel(@RequestBody Vessel vessel) {
        if (isValidType(vessel.getType())) {
            return ResponseEntity.ok(vesselService.createVessel(vessel.getType(), vessel.getColor()));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vessel> getVesselById(@PathVariable UUID id) {
        return vesselService.getVesselById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vessel> updateVessel(@PathVariable UUID id, @RequestBody Vessel vessel) {
        if (vesselService.getVesselById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else if (!isValidType(vessel.getType())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(vesselService.updateVessel(id, vessel.getType(), vessel.getColor()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVessel(@PathVariable UUID id) {
        if (vesselService.getVesselById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        vesselService.deleteVesselById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Vessel>> getAllVessels() {
        return ResponseEntity.ok(vesselService.getAllVessels());
    }

    @GetMapping("/color/{color}")
    public ResponseEntity<List<Vessel>> getVesselsByColor(@PathVariable String color) {
        return ResponseEntity.ok(vesselService.getVesselsByColor(color));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllVessels() {
        vesselService.deleteAllVessels();
        return ResponseEntity.noContent().build();
    }

    private boolean isValidType(String type) {
        return type.equals("Cargo") || type.equals("Tanker") || type.equals("Cruise") || type.equals("Fishing") || type.equals("Military");
    }
}