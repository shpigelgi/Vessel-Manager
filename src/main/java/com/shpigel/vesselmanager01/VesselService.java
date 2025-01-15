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

    public Vessel createVessel(String type, String color) {
        Vessel vessel = new Vessel();
        vessel.setType(type);
        vessel.setColor(color);
        return vesselRepository.save(vessel);
    }

    //TODO: figure out what to do if ID is not found
    public Optional<Vessel> getVesselById(UUID id) {
        return vesselRepository.findById(id);
    }

    public List<Vessel> getVesselsByColor(String color) {
        return vesselRepository.findByColor(color);
    }

    public void deleteVesselById(UUID id) {
        vesselRepository.deleteById(id);
    }

    public Vessel updateVessel(UUID id, String type, String color) {
        Vessel vessel = new Vessel();
        vessel.setId(id);
        vessel.setType(type);
        vessel.setColor(color);
        vesselRepository.deleteById(id);
        return vesselRepository.save(vessel);
    }
}
