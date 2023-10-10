package dk.lyngby.controller.impl;

import dk.lyngby.config.ApplicationConfig;
import dk.lyngby.config.HibernateConfig;
import dk.lyngby.config.Populate;
import dk.lyngby.dto.HotelDto;
import dk.lyngby.dto.RoomDto;
import dk.lyngby.model.Room;
import io.javalin.Javalin;
import io.javalin.http.ContentType;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

class RoomControllerTest {

    private static Javalin app;
    private static EntityManagerFactory emfTest;
    private static final String BASE_URL = "http://localhost:7777/api/v1";

    @BeforeAll
    static void beforeAll() {
        // Setup test database
        HibernateConfig.setTest(true);
        emfTest = HibernateConfig.getEntityManagerFactory();

        // Start server
        app = Javalin.create();
        ApplicationConfig.startServer(app, 7777);
    }

    @BeforeEach
    void setUp() {
        Populate.populateData(emfTest);
    }

    @AfterAll
    static void tearDown() {
        HibernateConfig.setTest(false);
        ApplicationConfig.stopServer(app);
    }

    @Test
    void read() {
        // given
        int roomId = 1;

        // when
        RoomDto room = given()
                .contentType("application/json")
                .when()
                .get(BASE_URL + "/rooms/" + roomId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .body().as(RoomDto.class);

        // then
        assertNotNull(room.getHotelId());
        assertTrue(room.getRoomNumber() > 0);
        assertTrue(room.getRoomNumber() > 99 && room.getRoomNumber() < 1000);
    }

    @Test
    @DisplayName("Read all rooms")
    void readAll() {
        // given
        int listSize = 15;

        // when
        given()
                .contentType("application/json")
                .when()
                .get(BASE_URL + "/rooms")
                .then()
                .assertThat()
                .statusCode(200)
                // then
                .body("size()", equalTo(listSize));
    }

    @Test
    void create() {
        // given
        Room room = new Room(123, new BigDecimal(2500), Room.RoomType.SINGLE);
        int hotelId = 1;

        // when
        HotelDto hotel =
                given()
                        .contentType(ContentType.JSON)
                        .body(room)
                        .when()
                        .post(BASE_URL + "/rooms/hotel/" + hotelId)
                        .then()
                        .statusCode(201)
                        .body("id", equalTo(hotelId))
                        .body("hotelName", equalTo("Hotel California"))
                        .body("hotelAddress", equalTo("California"))
                        .body("rooms", hasSize(7))
                        .extract().body().as(HotelDto.class);

        // then
        assertTrue(hotel.getRooms().contains(room.getRoomNumber()));
    }

    @Test
    void update() {
        // given
        Room update = new Room(100, new BigDecimal(10000), Room.RoomType.SINGLE);

        // when
        RoomDto updRoom =
                given()
                        .contentType("application/json")
                        .body(update)
                        .when()
                        .put(BASE_URL + "/rooms/1")
                        .then()
                        .assertThat()
                        .statusCode(200)
                        .extract().body().as(RoomDto.class);

        // then
        assertEquals(updRoom.getRoomPrice(), update.getRoomPrice());
    }

    @Test
    void delete() {

        // given
        int roomId = 2;

        // when
        given()
                .contentType("application/json")
                .when()
                .delete(BASE_URL + "/rooms/" + roomId)
                .then()
                .assertThat()
                .statusCode(204);

        // then
        given()
                .contentType("application/json")
                .when()
                .get(BASE_URL + "/rooms/" + roomId)
                .then()
                .assertThat()
                .statusCode(404);
    }
}