package dk.lyngby.config;

import dk.lyngby.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class Populate {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Delete all existing records from tables
            em.createQuery("DELETE FROM Rating").executeUpdate();
            em.createQuery("DELETE FROM Picture").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();


            // Persist new data
            Picture picture = new Picture("https://images.unsplash.com/photo-1701906268416-b461ec4caa34?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHwyfHx8fHx8Mnx8MTcwMjI5MzE1Mnw&ixlib=rb-4.0.3&q=80&w=400", "the sun is setting over the clouds in the sky", "John Doe", "john_doe", "https://unsplash.com/@johndoe", "https://unsplash.com/@johndoe");
            User user4 = new User("user4", "user4");
            User admin = new User("admin", "admin");
            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            user4.addPicture(picture);
            user4.addRole(userRole);
            admin.addRole(adminRole);
            Rating rating = new Rating(3, user4);
            picture.addRating(rating);
            em.persist(rating);
            em.persist(user4);
            em.persist(admin);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(picture);


            em.getTransaction().commit();
        }
    }
}
