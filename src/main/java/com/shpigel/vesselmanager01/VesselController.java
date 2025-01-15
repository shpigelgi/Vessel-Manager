package com.shpigel.vesselmanager01;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/vessels")
public class VesselController {

    private final VesselService vesselService;

    public VesselController(VesselService vesselService) {
        this.vesselService = vesselService;
    }

    @PostMapping
    public ResponseEntity<Vessel> createVessel(@RequestParam String type, @RequestParam String color) {
        return ResponseEntity.ok(vesselService.createVessel(type, color));
    }

    @GetMapping
    public ResponseEntity<Vessel> getVesselById(@PathVariable UUID id) {
        return vesselService.getVesselById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vessel> updateVessel(@RequestBody Vessel vessel) {
        return ResponseEntity.ok(vesselService.updateVessel(vessel.getId(), vessel.getType(), vessel.getColor()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVessel(@PathVariable UUID id) {
        vesselService.deleteVesselById(id);
        return ResponseEntity.noContent().build();
    }
}