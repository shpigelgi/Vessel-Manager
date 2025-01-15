package com.shpigel.vesselmanager01;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VesselRepository extends JpaRepository<Vessel, UUID> {

    //READ: get a vessel by its ID
    Optional<Vessel> findById(UUID id);

    //DELETE: delete a vessel by its ID
    void deleteById(UUID id);

    //READ: find all vessels of a certain color
    List<Vessel> findByColor(String color);

}
