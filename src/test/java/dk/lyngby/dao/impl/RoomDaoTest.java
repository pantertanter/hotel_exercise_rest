package dk.lyngby.dao.impl;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.config.Populate;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class RoomDaoTest {

    private static RoomDao roomDao;
    private static EntityManagerFactory emfTest;


    @BeforeAll
    static void setUpAll() {
        HibernateConfig.setTest(true);
        emfTest = HibernateConfig.getEntityManagerFactory();
        roomDao = RoomDao.getInstance(emfTest);
    }

    @BeforeEach
    void setUp() {
        Populate.populateData(emfTest);
    }

    @AfterAll
    static void tearDownAll() {
        HibernateConfig.setTest(false);
    }


    @Test
    void addRoomToHotel() {
    }

    @Test
    void read() {
    }

    @Test
    void readAll() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void validatePrimaryKey() {
    }

    @Test
    void validateHotelRoomNumber() {
    }
}