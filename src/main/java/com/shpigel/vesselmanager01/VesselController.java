package com.shpigel.vesselmanager01;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for handling HTTP requests related to vessels.
 */
@RestController
@RequestMapping("/vessels")
public class VesselController {

    private final VesselService vesselService;

    public VesselController(VesselService vesselService) {
        this.vesselService = vesselService;
    }

    /**
     * Creates a new vessel.
     *
     * @param vessel the vessel to create
     * @return the created vessel
     */
    @PostMapping
    public ResponseEntity<Vessel> createVessel(@RequestBody Vessel vessel) {
        if (isValidType(vessel.getType())) {
            return ResponseEntity.ok(vesselService.createVessel(vessel.getType(), vessel.getColor()));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Retrieves a vessel by its id.
     *
     * @param id the id of the vessel
     * @return the vessel if found, otherwise a 404 response
     */
    @GetMapping("/{id}")
    public ResponseEntity<Vessel> getVesselById(@PathVariable UUID id) {
        return vesselService.getVesselById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    /**
     * Updates a vessel by its id.
     *
     * @param id     the id of the vessel
     * @param vessel the updated vessel
     * @return the updated vessel if found, otherwise a 404 response
     */
    @PutMapping("/{id}")
    public ResponseEntity<Vessel> updateVessel(@PathVariable UUID id, @RequestBody Vessel vessel) {
        if (vesselService.getVesselById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else if (!isValidType(vessel.getType())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(vesselService.updateVessel(id, vessel.getType(), vessel.getColor()));
    }

    /**
     * Deletes a vessel by its id.
     *
     * @param id the id of the vessel
     * @return a 204 response if the vessel was deleted, otherwise a 404 response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVessel(@PathVariable UUID id) {
        if (vesselService.getVesselById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        vesselService.deleteVesselById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all vessels.
     *
     * @return a list of all vessels
     */
    @GetMapping
    public ResponseEntity<List<Vessel>> getAllVessels() {
        return ResponseEntity.ok(vesselService.getAllVessels());
    }

    /**
     * Retrieves all vessels of a certain color.
     *
     * @param color the color of the vessels
     * @return a list of vessels with the specified color
     */
    @GetMapping("/color/{color}")
    public ResponseEntity<List<Vessel>> getVesselsByColor(@PathVariable String color) {
        return ResponseEntity.ok(vesselService.getVesselsByColor(color));
    }

    /**
     * Deletes all vessels.
     *
     * @return a 204 response
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllVessels() {
        vesselService.deleteAllVessels();
        return ResponseEntity.noContent().build();
    }

    /**
     * Checks if the vessel type is valid.
     * The type is valid if it is one of the following: Cargo, Tanker, Cruise, Fishing, Military
     *
     * @param type the type of the vessel
     * @return true if the type is valid, false otherwise
     */
    private boolean isValidType(String type) {
        return type.equals("Cargo") || type.equals("Tanker") || type.equals("Cruise") || type.equals("Fishing") || type.equals("Military");
    }
}