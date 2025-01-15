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
        if (vessel.getType().equals("Cargo") || vessel.getType().equals("Tanker") || vessel.getType().equals("Cruise") || vessel.getType().equals("Fishing") || vessel.getType().equals("Military")) {
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
        return ResponseEntity.ok(vesselService.updateVessel(id, vessel.getType(), vessel.getColor()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVessel(@PathVariable UUID id) {
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
}