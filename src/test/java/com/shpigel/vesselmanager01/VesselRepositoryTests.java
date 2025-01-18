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

@DataJpaTest
@Testcontainers
public class VesselRepositoryTests {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.2").withDatabaseName("testdb").withUsername("vessel_manager").withPassword("vessel_manager");

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
        Assertions.assertEquals(1, vesselRepository.count());
        Assertions.assertEquals("Blue", v.getColor());
        Assertions.assertEquals("Cargo", v.getType());
        Assertions.assertNotNull(v.getId());
    }

    @Test
    void testFindVesselById() {
        Vessel vessel = new Vessel();
        vessel.setType("Tanker");
        vessel.setColor("Red");
        Vessel savedVessel = vesselRepository.save(vessel);
        Vessel fetchedVessel = vesselRepository.findById(savedVessel.getId()).orElse(null);
        Assertions.assertNotNull(fetchedVessel);
        Assertions.assertEquals(savedVessel.getId(), fetchedVessel.getId());
        Assertions.assertEquals("Red", fetchedVessel.getColor());
        Assertions.assertEquals("Tanker", fetchedVessel.getType());
    }

    @Test
    void testUpdateVessel() {
        Vessel vessel = new Vessel();
        vessel.setType("Fishing");
        vessel.setColor("Green");
        Vessel savedVessel = vesselRepository.save(vessel);
        savedVessel.setColor("Yellow");
        Vessel updatedVessel = vesselRepository.save(savedVessel);
        Assertions.assertEquals("Yellow", updatedVessel.getColor());
        Assertions.assertEquals("Fishing", updatedVessel.getType());
    }

    @Test
    void testDeleteVessel() {
        Vessel vessel = new Vessel();
        vessel.setType("Cruise");
        vessel.setColor("White");
        Vessel savedVessel = vesselRepository.save(vessel);
        vesselRepository.delete(savedVessel);
        Assertions.assertEquals(0, vesselRepository.count());
    }


}
