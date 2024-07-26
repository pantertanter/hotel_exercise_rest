package dk.lyngby.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.lyngby.config.ApplicationConfig;
import dk.lyngby.config.HibernateConfig;
import dk.lyngby.model.Picture;
import dk.lyngby.model.Rating;
import dk.lyngby.model.Role;
import dk.lyngby.model.User;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class PictureDaoTest {

    private static Javalin app;
    private static final String BASE_URL = "http://localhost:7777/api/v1";
    private static PictureDao pictureDao;
    private static EntityManagerFactory emfTest;
    private static String adminToken;
    private static String userToken;
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static User user, admin;
    private static Role userRole, adminRole;

    @BeforeAll
    static void beforeAll() {
        HibernateConfig.setTest(true);
        emfTest = HibernateConfig.getEntityManagerFactory();
        pictureDao = new PictureDao();
        app = Javalin.create();
        ApplicationConfig.startServer(app, 7777);
        RestAssured.baseURI = BASE_URL;
    }

    @BeforeEach
    void setUp() {
        try (EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();

            // Delete all existing records from tables
            em.createQuery("DELETE FROM Rating").executeUpdate();
            em.createQuery("DELETE FROM Picture").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();

            // Create and persist new roles
            userRole = new Role("user");
            adminRole = new Role("admin");
            em.persist(userRole);
            em.persist(adminRole);

            // Create and persist new users
            user = new User("user", "user");
            admin = new User("admin", "admin");

            user.addRole(userRole);
            admin.addRole(adminRole);

            em.persist(user);
            em.persist(admin);

            // Create and persist new pictures and ratings
            Picture picture_1 = new Picture(
                    "https://images.unsplash.com/photo-1611288870280-4a322b8ec7ec?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfHNlYXJjaHwxfHx3cmVuY2h8ZW58MHx8fHwxNzIxMTE1NDUyfDA&ixlib=rb-4.0.3&q=80&w=400",
                    "silver steel tool on white surface",
                    "Recha Oktaviani",
                    "rechaoktaviani",
                    "https://unsplash.com/@rechaoktaviani",
                    "https://api.unsplash.com/photos/t__61ap00Mc/download?ixid=M3w1Mzk1MTR8MHwxfHNlYXJjaHwxfHx3cmVuY2h8ZW58MHx8fHwxNzIxMTE1NDUyfDA"
            );

            user.addPicture(picture_1);
            Rating rating_1 = new Rating(3, user);
            picture_1.addRating(rating_1);

            em.persist(rating_1);
            em.persist(picture_1);

            em.getTransaction().commit();
        }

        adminToken = getToken(admin.getUsername(), "admin");
        userToken = getToken(user.getUsername(), "user");
    }

    @AfterAll
    static void tearDown() {
        HibernateConfig.setTest(false);
        ApplicationConfig.stopServer(app);
    }

    @Test
    void addPictureToUser() {
        Picture picture = new Picture(
                "https://images.unsplash.com/photo-1720887237185-6b8d5c01005d?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHw4fHx8fHx8Mnx8MTcyMTExNTQzOXw&ixlib=rb-4.0.3&q=80&w=400",
                "A couple of people that are looking in a window",
                "James Chan",
                "jvmesc_",
                "https://unsplash.com/@jvmesc_",
                "https://api.unsplash.com/photos/KWocLB1EHIc/download?ixid=M3w1Mzk1MTR8MHwxfGFsbHw4fHx8fHx8Mnx8MTcyMTExNTQzOXw"
        );

        try {
            String pictureJson = objectMapper.writeValueAsString(picture);

            given()
                    .header("Authorization", userToken)
                    .contentType("application/json")
                    .body(pictureJson)
                    .when()
                    .post(BASE_URL + "/pictures/user")
                    .then()
                    .statusCode(201);
        } catch (Exception e) {
            fail("Failed to serialize picture to JSON: " + e.getMessage());
        }
    }

    @Test
    void readAllPicturesFromUser() {
        given()
                .header("Authorization", userToken)
                .when()
                .get(BASE_URL + "/pictures/" + user.getUsername())
                .then()
                .statusCode(200);
    }

    @Test
    void deleteAllPicturesFromUser() {
        given()
                .header("Authorization", userToken)
                .when()
                .delete(BASE_URL + "/pictures/user")
                .then()
                .statusCode(204);
    }

    @Test
    void deletePictureFromUser() {
        given()
                .header("Authorization", userToken)
                .when()
                .delete(BASE_URL + "/pictures/1")
                .then()
                .statusCode(204);
    }

    private static String getToken(String username, String password) {
        String json = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

        return given()
                .contentType("application/json")
                .body(json)
                .when()
                .post(BASE_URL + "/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .body()
                .path("token");
    }
}
