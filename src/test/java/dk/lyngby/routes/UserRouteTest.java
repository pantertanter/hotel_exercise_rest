package dk.lyngby.routes;

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
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

class UserRouteTest {

    private static Javalin app;
    private static final String BASE_URL = "http://localhost:7777/api/v1";
    private static EntityManagerFactory emfTest;
    private static String adminToken;
    private static String user_1Token;
    private static String user_2Token;
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static User user_1, user_2, admin;

    @BeforeAll
    static void beforeAll() {
        HibernateConfig.setTest(true);
        emfTest = HibernateConfig.getEntityManagerFactory();
        app = Javalin.create();
        ApplicationConfig.startServer(app, 7777);
        RestAssured.baseURI = BASE_URL;

        try (EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");

            user_1 = new User("user_1", "user_1");
            user_2 = new User("user_2", "user_2");
            admin = new User("admin", "admin");

            user_1.addRole(userRole);
            user_2.addRole(userRole);
            admin.addRole(adminRole);

            em.persist(user_1);
            em.persist(user_2);
            em.persist(admin);

            em.persist(userRole);
            em.persist(adminRole);

            em.getTransaction().commit();
        }
    }

    @BeforeEach
    void setUp() {
        try (EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();

            // Delete all existing records from tables
            em.createQuery("DELETE FROM Rating").executeUpdate();
            em.createQuery("DELETE FROM Picture").executeUpdate();

            // Persist new data
            Picture picture_1 = new Picture("https://images.unsplash.com/photo-1611288870280-4a322b8ec7ec?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfHNlYXJjaHwxfHx3cmVuY2h8ZW58MHx8fHwxNzIxMTE1NDUyfDA&ixlib=rb-4.0.3&q=80&w=400", "silver steel tool on white surface", "Recha Oktaviani", "rechaoktaviani", "https://unsplash.com/@rechaoktaviani", "https://api.unsplash.com/photos/t__61ap00Mc/download?ixid=M3w1Mzk1MTR8MHwxfHNlYXJjaHwxfHx3cmVuY2h8ZW58MHx8fHwxNzIxMTE1NDUyfDA");
            Picture picture_2 = new Picture("https://images.unsplash.com/photo-1720719908968-f6bcf72b8ce2?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHw0fHx8fHx8Mnx8MTcyMDgwNDg5MHw&ixlib=rb-4.0.3&q=80&w=400", "A red building with red balconies and a clock", "Pascal Bullan", "jetztabertempo", "https://unsplash.com/@jetztabertempo", "https://api.unsplash.com/photos/WsdsKOTNEA0/download?ixid=M3w1Mzk1MTR8MHwxfGFsbHw0fHx8fHx8Mnx8MTcyMDgwNDg5MHw");
            Picture picture_3 = new Picture("https://images.unsplash.com/photo-1720766595377-b413ce3a6478?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHw1fHx8fHx8Mnx8MTcyMDgwNDg5MHw&ixlib=rb-4.0.3&q=80&w=400", "A person standing in front of a bush with white flowers", "Max Ovcharenko", "chestmax", "https://unsplash.com/@chestmax", "https://api.unsplash.com/photos/PH-kwn9zkf8/download?ixid=M3w1Mzk1MTR8MHwxfGFsbHw1fHx8fHx8Mnx8MTcyMDgwNDg5MHw");
            Picture picture_4 = new Picture("https://images.unsplash.com/photo-1720766255393-60ca5199def4?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHw2fHx8fHx8Mnx8MTcyMDgwNDg5MHw&ixlib=rb-4.0.3&q=80&w=400", "Three pink roses in a vase on a black background", "ISO10", "isoten", "https://unsplash.com/@isoten", "https://api.unsplash.com/photos/MJJ8p9b2AvY/download?ixid=M3w1Mzk1MTR8MHwxfGFsbHw2fHx8fHx8Mnx8MTcyMDgwNDg5MHw");
            Picture picture_5 = new Picture("https://images.unsplash.com/photo-1721023672487-906b07479bff?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHwyfHx8fHx8Mnx8MTcyMTExNTQzOXw&ixlib=rb-4.0.3&q=80&w=400", "A wooden table topped with a vase filled with flowers", "Parker Coffman", "lowmurmer", "https://unsplash.com/@lowmurmer", "https://api.unsplash.com/photos/jGDqRxc_xQ8/download?ixid=M3w1Mzk1MTR8MHwxfGFsbHwyfHx8fHx8Mnx8MTcyMTExNTQzOXw");
            Picture picture_6 = new Picture("https://images.unsplash.com/photo-1720887237185-6b8d5c01005d?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHw4fHx8fHx8Mnx8MTcyMTExNTQzOXw&ixlib=rb-4.0.3&q=80&w=400", "A couple of people that are looking in a window", "James Chan", "jvmesc_", "https://unsplash.com/@jvmesc_", "https://api.unsplash.com/photos/KWocLB1EHIc/download?ixid=M3w1Mzk1MTR8MHwxfGFsbHw4fHx8fHx8Mnx8MTcyMTExNTQzOXw");

            user_1.addPicture(picture_1);
            user_1.addPicture(picture_2);
            user_2.addPicture(picture_3);
            user_2.addPicture(picture_4);
            admin.addPicture(picture_5);
            admin.addPicture(picture_6);

            Rating rating_1 = new Rating(3, user_1);
            Rating rating_2 = new Rating(4, user_1);
            Rating rating_3 = new Rating(5, user_2);
            Rating rating_4 = new Rating(2, user_2);
            Rating rating_5 = new Rating(1, admin);
            Rating rating_6 = new Rating(5, admin);

            picture_1.addRating(rating_1);
            picture_2.addRating(rating_2);
            picture_3.addRating(rating_3);
            picture_4.addRating(rating_4);
            picture_5.addRating(rating_5);
            picture_6.addRating(rating_6);

            em.persist(rating_1);
            em.persist(rating_2);
            em.persist(rating_3);
            em.persist(rating_4);
            em.persist(rating_5);
            em.persist(rating_6);

            em.persist(picture_1);
            em.persist(picture_2);
            em.persist(picture_3);
            em.persist(picture_4);
            em.persist(picture_5);
            em.persist(picture_6);

            em.getTransaction().commit();
        }

        adminToken = getToken(admin.getUsername(), "admin");
        user_1Token = getToken(user_1.getUsername(), "user_1");
        user_2Token = getToken(user_2.getUsername(), "user_2");
    }

    @AfterAll
    static void tearDown() {
        try (EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Rating").executeUpdate();
            em.createQuery("DELETE FROM Picture").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();
            em.getTransaction().commit();
        }

        HibernateConfig.setTest(false);
        ApplicationConfig.stopServer(app);
    }

    @Test
    void login() {
        given()
                .contentType("application/json")
                .body("{\"username\": \"user_1\", \"password\": \"user_1\"}")
                .when()
                .post(BASE_URL + "/auth/login")
                .then()
                .statusCode(200);

    }

    @Test
    void register() {
        given()
                .contentType("application/json")
                .body("{\"username\": \"user_3\", \"password\": \"user_3\", \"role\": \"user\"}")
                .when()
                .post(BASE_URL + "/auth/register")
                .then()
                .statusCode(201);
    }

    @Test
    void getAllUsers() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType("application/json")
                .when()
                .get(BASE_URL + "/auth/getAllUsers")
                .then()
                .statusCode(200);
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