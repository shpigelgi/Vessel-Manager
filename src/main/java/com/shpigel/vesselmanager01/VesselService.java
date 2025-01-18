package com.shpigel.vesselmanager01;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VesselService {

    private final VesselRepository vesselRepository;

    public VesselService(VesselRepository vesselRepository) {
        this.vesselRepository = vesselRepository;
    }

    /**
     * Creates a new vessel with the given type and color.
     *
     * @param type  the type of the vessel
     * @param color the color of the vessel
     * @return the created vessel
     */
    public Vessel createVessel(String type, String color) {
        Vessel vessel = new Vessel();
        vessel.setType(type);
        vessel.setColor(color);
        return vesselRepository.save(vessel);
    }

    /**
     * Retrieves a vessel by its id.
     *
     * @param id the id of the vessel
     * @return the vessel if found, otherwise an empty optional
     */
    public Optional<Vessel> getVesselById(UUID id) {
        return vesselRepository.findById(id);
    }

    /**
     * Retrieves all vessels of a certain color.
     *
     * @param color the color of the vessels
     * @return a list of vessels with the given color
     */
    public List<Vessel> getVesselsByColor(String color) {
        return vesselRepository.findByColor(color);
    }

    /**
     * Deletes a vessel by its id.
     *
     * @param id the id of the vessel to delete
     */
    public void deleteVesselById(UUID id) {
        vesselRepository.deleteById(id);
    }

    /**
     * Updates a vessel with the given id, type, and color.
     *
     * @param id    the id of the vessel to update
     * @param type  the new type of the vessel
     * @param color the new color of the vessel
     * @return the updated vessel
     */
    public Vessel updateVessel(UUID id, String type, String color) {
        Vessel vessel = new Vessel();
        vessel.setId(id);
        vessel.setType(type);
        vessel.setColor(color);
        vesselRepository.deleteById(id);
        return vesselRepository.save(vessel);
    }

    /**
     * Retrieves all vessels.
     *
     * @return a list of all vessels
     */
    public List<Vessel> getAllVessels() {
        return vesselRepository.findAll();
    }

    /**
     * Deletes all vessels.
     */
    public void deleteAllVessels() {
        vesselRepository.deleteAll();
    }

}
