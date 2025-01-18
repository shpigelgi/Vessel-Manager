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
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.2").withDatabaseName("testdb").withUsername("vessel_manager").withPassword("vessel_manager");

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
        Assertions.assertEquals(1, vesselController.getAllVessels().getBody().size());
        Assertions.assertNotNull(v);
        Assertions.assertEquals("Blue", v.getColor());
        Assertions.assertEquals("Cargo", v.getType());
        Assertions.assertNotNull(v.getId());

        //test fail case
        Vessel vessel2 = new Vessel();
        vessel2.setType("Submarine");
        vessel2.setColor("Black");
        Assertions.assertTrue(vesselController.createVessel(vessel2).getStatusCode().is4xxClientError());
    }

    @Test
    void testFindVesselById() {
        Vessel vessel = new Vessel();
        vessel.setType("Tanker");
        vessel.setColor("Red");
        Vessel savedVessel = vesselController.createVessel(vessel).getBody();
        Assertions.assertNotNull(savedVessel);
        Vessel fetchedVessel = vesselController.getVesselById(savedVessel.getId()).getBody();
        Assertions.assertNotNull(fetchedVessel);
        Assertions.assertEquals(savedVessel.getId(), fetchedVessel.getId());

        //test fail case - the probability of getting a random UUID that is already in the database is very low
        Assertions.assertTrue(vesselController.getVesselById(UUID.randomUUID()).getStatusCode().is4xxClientError());
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
        Assertions.assertNotNull(fetchedVessel);
        Assertions.assertEquals(savedVessel.getId(), fetchedVessel.getId());
        Assertions.assertEquals("Red", fetchedVessel.getColor());
        Assertions.assertEquals("Tanker", fetchedVessel.getType());

        //test fail case
        updatedVessel.setType("Submarine");
        Assertions.assertTrue(vesselController.updateVessel(savedVessel.getId(), updatedVessel).getStatusCode().is4xxClientError());
        Assertions.assertTrue(vesselController.updateVessel(UUID.randomUUID(), updatedVessel).getStatusCode().is4xxClientError());
    }

    @Test
    void testDeleteVessel() {
        Vessel vessel = new Vessel();
        vessel.setType("Cruise");
        vessel.setColor("White");
        Vessel savedVessel = vesselController.createVessel(vessel).getBody();
        Assertions.assertNotNull(savedVessel);
        vesselController.deleteVessel(savedVessel.getId());
        Assertions.assertTrue(vesselController.getAllVessels().getBody().isEmpty());

        //test fail case
        Assertions.assertTrue(vesselController.deleteVessel(savedVessel.getId()).getStatusCode().is4xxClientError());
        Assertions.assertTrue(vesselController.deleteVessel(UUID.randomUUID()).getStatusCode().is4xxClientError());
    }

    @Test
    void testGetVesselsByColor() {
        Vessel vessel = new Vessel();
        vessel.setType("Cruise");
        vessel.setColor("White");
        vesselController.createVessel(vessel);
        Assertions.assertEquals(1, vesselController.getVesselsByColor("White").getBody().size());

        //test fail case
        Assertions.assertTrue(vesselController.getVesselsByColor("Black").getBody().isEmpty());
    }
}
