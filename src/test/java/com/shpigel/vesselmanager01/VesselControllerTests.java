package com.shpigel.vesselmanager01;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

@DataJpaTest
@Testcontainers
public class VesselControllerTests {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.2")
            .withDatabaseName("testdb")
            .withUsername("vessel_manager")
            .withPassword("vessel_manager");

    @Autowired
    private VesselRepository vesselRepository;

    private VesselController vesselController;

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        vesselRepository.deleteAll();
        vesselController = new VesselController(new VesselService(vesselRepository));
    }

    @Test
    void testCreateVessel() {
        //test pass cases
        Vessel vessel = new Vessel();
        vessel.setType("Cargo");
        vessel.setColor("Blue");
        Vessel v = vesselController.createVessel(vessel).getBody();
        assert vesselController.getAllVessels().getBody().size() == 1;
        assert v.getColor().equals("Blue");
        assert v.getType().equals("Cargo");
        assert v.getId() != null;

        //test fail case
        Vessel vessel2 = new Vessel();
        vessel2.setType("Submarine");
        vessel2.setColor("Black");
        assert vesselController.createVessel(vessel2).getStatusCode().is4xxClientError();
    }

    @Test
    void testFindVesselById() {
        Vessel vessel = new Vessel();
        vessel.setType("Tanker");
        vessel.setColor("Red");
        Vessel savedVessel = vesselController.createVessel(vessel).getBody();
        Vessel fetchedVessel = vesselController.getVesselById(savedVessel.getId()).getBody();
        assert fetchedVessel != null;
        assert fetchedVessel.getId().equals(savedVessel.getId());

        //test fail case - the probability of getting a random UUID that is already in the database is very low
        assert vesselController.getVesselById(UUID.randomUUID()).getStatusCode().is4xxClientError();
    }

    @Test
    void testUpdateVessel() {
        Vessel vessel = new Vessel();
        vessel.setType("Cargo");
        vessel.setColor("Blue");
        Vessel savedVessel = vesselController.createVessel(vessel).getBody();
        Vessel updatedVessel = new Vessel();
        updatedVessel.setType("Tanker");
        updatedVessel.setColor("Red");
        Vessel fetchedVessel = vesselController.updateVessel(savedVessel.getId(), updatedVessel).getBody();
        assert fetchedVessel != null;
        assert fetchedVessel.getId().equals(savedVessel.getId());
        assert fetchedVessel.getColor().equals("Red");
        assert fetchedVessel.getType().equals("Tanker");

        //test fail case
        Assertions.assertTrue(vesselController.updateVessel(UUID.randomUUID(), updatedVessel).getStatusCode().is4xxClientError());
    }

    @Test
    void testDeleteVessel() {
        Vessel vessel = new Vessel();
        vessel.setType("Cruise");
        vessel.setColor("White");
        Vessel savedVessel = vesselController.createVessel(vessel).getBody();
        vesselController.deleteVessel(savedVessel.getId());
        assert vesselController.getAllVessels().getBody().isEmpty();

        //test fail case
        Assertions.assertTrue(vesselController.deleteVessel(UUID.randomUUID()).getStatusCode().is4xxClientError());

    }

    @Test
    void testGetVesselsByColor() {
        Vessel vessel = new Vessel();
        vessel.setType("Cruise");
        vessel.setColor("White");
        Vessel savedVessel = vesselController.createVessel(vessel).getBody();
        Assertions.assertEquals(1, vesselController.getVesselsByColor("White").getBody().size());

        //test fail case
        Assertions.assertTrue(vesselController.getVesselsByColor("Black").getBody().isEmpty());
    }
}
