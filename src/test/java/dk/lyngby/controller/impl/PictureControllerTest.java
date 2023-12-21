package dk.lyngby.controller.impl;

import dk.lyngby.config.ApplicationConfig;
import dk.lyngby.config.HibernateConfig;
import dk.lyngby.dto.PictureDto;
import dk.lyngby.model.Picture;
import dk.lyngby.model.Rating;
import dk.lyngby.model.Role;
import dk.lyngby.model.User;
import io.javalin.Javalin;
import io.javalin.http.ContentType;
import jakarta.persistence.EntityManagerFactory;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.shaded.org.hamcrest.Matchers.containsInAnyOrder;

class PictureControllerTest {

    private static Javalin app;
    private static final String BASE_URL = "http://localhost:7777/api/v1";
    private static PictureController pictureController;

    private static EntityManagerFactory emfTest;
    private static Object adminToken;
    private static Object userToken;

    private static Picture p1, p2;
    private static User user, admin;
    private static Role userRole, adminRole;

    @BeforeAll
    static void beforeAll() {
        HibernateConfig.setTest(true);
        emfTest = HibernateConfig.getEntityManagerFactory();
        pictureController = new PictureController();
        app = Javalin.create();
        ApplicationConfig.startServer(app, 7777);

        // Create users and roles
        user = new User("usertest", "user123");
        admin = new User("admintest", "admin123");
        userRole = new Role("user");
        adminRole = new Role("admin");
        user.addRole(userRole);
        admin.addRole(adminRole);
        try (var em = emfTest.createEntityManager())
        {
            em.getTransaction().begin();
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.getTransaction().commit();
        }

        // Get tokens
        UserController userController = new UserController();
        adminToken = getToken(admin.getUsername(), "admin123");
        userToken = getToken(user.getUsername(), "user123");
    }

    @BeforeEach
    void setUp() {

        List<Rating> pic1Rats = getPic1Rats();
        List<Rating> pic2Rats = getPic2Rats();

        try (var em = emfTest.createEntityManager())
        {

            em.getTransaction().begin();

            // Delete all rows
            em.createQuery("DELETE FROM Rating r").executeUpdate();
            em.createQuery("DELETE FROM Picture p").executeUpdate();

            // Reset sequence
            em.createNativeQuery("ALTER SEQUENCE rating_rating_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE picture_picture_id_seq RESTART WITH 1").executeUpdate();

            // Insert test data for hotels and rooms
            p1 = new Picture("src for p1", "alt for p1");
            p2 = new Picture("src for p2", "alt for p2");
            p1.setRatings(pic1Rats);
            p2.setRatings(pic2Rats);
            em.persist(p1);
            em.persist(p2);

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
    void readAll() {
        // Given -> When -> Then
        List<PictureDto> pictureDtoList =
                given()
                        .contentType("application/json")
                        .when()
                        .get(BASE_URL + "/pictures")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK_200)  // could also just be 200
                        .extract().body().jsonPath().getList("", PictureDto.class);

        PictureDto p1DTO = new PictureDto(p1);
        PictureDto p2DTO = new PictureDto(p2);

        assertEquals(pictureDtoList.size(), 2);
    }

    @Test
    void addPictureToUser() {
        Picture p3 = new Picture("url for picture 3", "alt for picture 3");
        given()
                .header("Authorization", adminToken)
                .contentType(ContentType.JSON)
                .body(p3)
                .when()
                .post(BASE_URL + "/pictures/admintest")
                .then()
                .statusCode(201)
                .body("id", equalTo(3))
                .body("url", equalTo("url for picture 3"))
                .body("alt", equalTo("alt for picture 3"));
    }
    @Test
    void readAllPicturesFromUser() {
        List<PictureDto> pictureDtoList =
                given()
                        .header("Authorization", adminToken)
                        .contentType(ContentType.JSON)
                        .when()
                        .get(BASE_URL + "/pictures/admintest")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK_200)  // could also just be 200
                        .extract().body().jsonPath().getList("", PictureDto.class);


        assertEquals(pictureDtoList.size(), 0);
    }

    @Test
    void deletePictureFromUser() {
        given()
                .header("Authorization", adminToken)
                .contentType("application/json")
                .when()
                .delete(BASE_URL + "/pictures/" + p1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200);
    }

    @NotNull
    private static List<Rating> getPic1Rats()
    {
        Rating r111 = new Rating(1);
        Rating r112 = new Rating(2);
        Rating r113 = new Rating(3);
        Rating r114 = new Rating(4);
        Rating r115 = new Rating(5);
        Rating r116 = new Rating(3);

        Rating[] ratArray = {r111, r112, r113, r114, r115, r116};
        return List.of(ratArray);
    }

    @NotNull
    private static List<Rating> getPic2Rats()
    {
        Rating r111 = new Rating(5);
        Rating r112 = new Rating(4);
        Rating r113 = new Rating(3);
        Rating r114 = new Rating(2);
        Rating r115 = new Rating(1);
        Rating r116 = new Rating(3);

        Rating[] ratArray = {r111, r112, r113, r114, r115, r116};
        return List.of(ratArray);
    }

    public static Object getToken(String username, String password)
    {
        return login(username, password);
    }

    private static Object login(String username, String password)
    {
        String json = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

        var token = given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("http://localhost:7777/api/v1/auth/login")
                .then()
                .extract()
                .response()
                .body()
                .path("token");

        return "Bearer " + token;
    }
}