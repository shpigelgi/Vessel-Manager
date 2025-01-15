package com.shpigel.vesselmanager01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
public class VesselRepositoryTests {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.2")
            .withDatabaseName("testdb")
            .withUsername("vessel_manager")
            .withPassword("vessel_manager");

    @Autowired
    private VesselRepository vesselRepository;

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        vesselRepository.deleteAll();
    }

    @Test
    void testCreateVessel() {

        //test pass cases
        Vessel vessel = new Vessel();
        vessel.setType("Cargo");
        vessel.setColor("Blue");
        Vessel v = vesselRepository.save(vessel);
        assert vesselRepository.count() == 1;
        assert v.getColor().equals("Blue");
        assert v.getType().equals("Cargo");
        assert v.getId() != null;
    }

    @Test
    void testFindVesselById() {
        Vessel vessel = new Vessel();
        vessel.setType("Tanker");
        vessel.setColor("Red");
        Vessel savedVessel = vesselRepository.save(vessel);
        Vessel fetchedVessel = vesselRepository.findById(savedVessel.getId()).orElse(null);
        assert fetchedVessel != null;
        assert fetchedVessel.getId().equals(savedVessel.getId());
        assert fetchedVessel.getColor().equals("Red");
        assert fetchedVessel.getType().equals("Tanker");
    }

    @Test
    void testUpdateVessel() {
        Vessel vessel = new Vessel();
        vessel.setType("Fishing");
        vessel.setColor("Green");
        Vessel savedVessel = vesselRepository.save(vessel);
        savedVessel.setColor("Yellow");
        Vessel updatedVessel = vesselRepository.save(savedVessel);
        assert updatedVessel.getColor().equals("Yellow");
        assert updatedVessel.getType().equals("Fishing");
    }

    @Test
    void testDeleteVessel() {
        Vessel vessel = new Vessel();
        vessel.setType("Cruise");
        vessel.setColor("White");
        Vessel savedVessel = vesselRepository.save(vessel);
        vesselRepository.delete(savedVessel);
        assert vesselRepository.count() == 0;
    }

    

}
