package dk.lyngby.config;


import dk.lyngby.model.*;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Set;

public class Populate {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Picture picture = new Picture("https://images.unsplash.com/photo-1701906268416-b461ec4caa34?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHwyfHx8fHx8Mnx8MTcwMjI5MzE1Mnw&ixlib=rb-4.0.3&q=80&w=400", "the sun is setting over the clouds in the sky");
            Rating rating = new Rating(3);
            picture.addRating(rating);
            User user4 = new User("user4", "user4");
            Role user = new Role("user");
            user4.addPicture(picture);
            user4.addRole(user);
            em.persist(rating);
            em.persist(user4);
            em.persist(user);
            em.persist(picture);
            em.getTransaction().commit();
        }
    }
}
