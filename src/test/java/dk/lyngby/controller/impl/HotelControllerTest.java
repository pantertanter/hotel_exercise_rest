/*
package dk.lyngby.controller.impl;

import dk.lyngby.config.ApplicationConfig;
import dk.lyngby.config.HibernateConfig;
import dk.lyngby.dto.PictureDto;
import dk.lyngby.model.Hotel;
import io.javalin.Javalin;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManagerFactory;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HotelControllerTest
{
    private static Javalin app;
    private static final String BASE_URL = "http://localhost:7777/api/v1";
    private static HotelController hotelController;
    private static EntityManagerFactory emfTest;

    private static Hotel h1, h2;

    @BeforeAll
    static void beforeAll()
    {
        HibernateConfig.setTest(true);
        emfTest = HibernateConfig.getEntityManagerFactory();
        hotelController = new HotelController();
        app = Javalin.create();
        ApplicationConfig.startServer(app, 7777);
    }

    @BeforeEach
    void setUp()
    {
        try (var em = emfTest.createEntityManager())
        {
            em.getTransaction().begin();
            // Delete all rows
            em.createQuery("DELETE FROM Picture p").executeUpdate();
            // Reset sequence
            em.createNativeQuery("ALTER SEQUENCE picture_picture_id_seq RESTART WITH 1").executeUpdate();
            // Insert test data

            em.getTransaction().commit();
        }
    }

    @AfterAll
    static void tearDown()
    {
        HibernateConfig.setTest(false);
        ApplicationConfig.stopServer(app);
    }

    @Test
    void read()
    {
        given()
                .contentType("application/json")
                .when()
                .get(BASE_URL + "/picture/" + p1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200)
                .body("id", equalTo(h1.getId()));
    }

    @Test
    void readAll()
    {
        // Given -> When -> Then
        List<PictureDto> pictureDtoList =
                given()
                        .contentType("application/json")
                        .when()
                        .get(BASE_URL + "/hotels")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK_200)  // could also just be 200
                        .extract().body().jsonPath().getList("", PictureDto.class);

        PictureDto h1DTO = new PictureDto(h1);
        PictureDto h2DTO = new PictureDto(h2);

        assertEquals(pictureDtoList.size(), 2);
        assertThat(pictureDtoList, containsInAnyOrder(h1DTO, h2DTO));
    }

    @Test
    void create()
    {
        Hotel h3 = new Hotel("Cab-inn", "Østergade 2", Hotel.HotelType.BUDGET);
        Room r1 = new Room(117, new BigDecimal(4500), Room.RoomType.SINGLE);
        Room r2 = new Room(118, new BigDecimal(2300), Room.RoomType.DOUBLE);
        h3.addRoom(r1);
        h3.addRoom(r2);
        PictureDto newHotel = new PictureDto(h3);

        List<RoomDto> roomDtos =
        given()
                .contentType(ContentType.JSON)
                .body(newHotel)
                .when()
                .post(BASE_URL + "/hotels")
                .then()
                .statusCode(201)
                .body("id", equalTo(3))
                .body("hotelName", equalTo("Cab-inn"))
                .body("hotelAddress", equalTo("Østergade 2"))
                .body("hotelType", equalTo("BUDGET"))
                .body("rooms", hasSize(2))
                .extract().body().jsonPath().getList("rooms", RoomDto.class);

        assertThat(roomDtos, containsInAnyOrder(new RoomDto(r1), new RoomDto(r2)));
    }

    @Test
    void update()
    {
        // Update the Bates Motel to luxury

        PictureDto updateHotel = new PictureDto("Bates Motel", "Lyngby" , Hotel.HotelType.LUXURY);
        given()
                .contentType(ContentType.JSON)
                .body(updateHotel)
                .log().all()
                .when()
                .put(BASE_URL + "/hotels/" + h2.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(h2.getId()))
                .body("hotelName", equalTo("Bates Motel"))
                .body("hotelAddress", equalTo("Lyngby"))
                .body("hotelType", equalTo("LUXURY"))
                .body("rooms", hasSize(6));
    }

    @Test
    void delete()
    {
        // Remove hotel California
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(BASE_URL + "/hotels/" + h1.getId())
                .then()
                .statusCode(204);

        // Check that it is gone
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(BASE_URL + "/hotels/" + h1.getId())
                .then()
                .statusCode(400);
    }

    @NotNull
    private static Set<Room> getCalRooms()
    {
        Room r100 = new Room(100, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r101 = new Room(101, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r102 = new Room(102, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r103 = new Room(103, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r104 = new Room(104, new BigDecimal(3200), Room.RoomType.DOUBLE);
        Room r105 = new Room(105, new BigDecimal(4500), Room.RoomType.SUITE);

        Room[] roomArray = {r100, r101, r102, r103, r104, r105};
        return Set.of(roomArray);
    }

    @NotNull
    private static Set<Room> getBatesRooms()
    {
        Room r111 = new Room(111, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r112 = new Room(112, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r113 = new Room(113, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r114 = new Room(114, new BigDecimal(2520), Room.RoomType.DOUBLE);
        Room r115 = new Room(115, new BigDecimal(3200), Room.RoomType.DOUBLE);
        Room r116 = new Room(116, new BigDecimal(4500), Room.RoomType.SUITE);

        Room[] roomArray = {r111, r112, r113, r114, r115, r116};
        return Set.of(roomArray);
    }
}*/
