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
            Picture picture_1 = new Picture("https://images.unsplash.com/photo-1611288870280-4a322b8ec7ec?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfHNlYXJjaHwxfHx3cmVuY2h8ZW58MHx8fHwxNzIxMTE1NDUyfDA&ixlib=rb-4.0.3&q=80&w=400", "silver steel tool on white surface", "Recha Oktaviani", "rechaoktaviani", "https://unsplash.com/@rechaoktaviani", "https://api.unsplash.com/photos/t__61ap00Mc/download?ixid=M3w1Mzk1MTR8MHwxfHNlYXJjaHwxfHx3cmVuY2h8ZW58MHx8fHwxNzIxMTE1NDUyfDA");
            Picture picture_2 = new Picture("https://images.unsplash.com/photo-1720719908968-f6bcf72b8ce2?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHw0fHx8fHx8Mnx8MTcyMDgwNDg5MHw&ixlib=rb-4.0.3&q=80&w=400", "A red building with red balconies and a clock", "Pascal Bullan", "jetztabertempo", "https://unsplash.com/@jetztabertempo", "https://api.unsplash.com/photos/WsdsKOTNEA0/download?ixid=M3w1Mzk1MTR8MHwxfGFsbHw0fHx8fHx8Mnx8MTcyMDgwNDg5MHw");
            Picture picture_3 = new Picture("https://images.unsplash.com/photo-1720766595377-b413ce3a6478?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHw1fHx8fHx8Mnx8MTcyMDgwNDg5MHw&ixlib=rb-4.0.3&q=80&w=400", "A person standing in front of a bush with white flowers", "Max Ovcharenko", "chestmax", "https://unsplash.com/@chestmax", "https://api.unsplash.com/photos/PH-kwn9zkf8/download?ixid=M3w1Mzk1MTR8MHwxfGFsbHw1fHx8fHx8Mnx8MTcyMDgwNDg5MHw");
            Picture picture_4 = new Picture("https://images.unsplash.com/photo-1720766255393-60ca5199def4?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHw2fHx8fHx8Mnx8MTcyMDgwNDg5MHw&ixlib=rb-4.0.3&q=80&w=400", "Three pink roses in a vase on a black background", "ISO10", "isoten", "https://unsplash.com/@isoten", "https://api.unsplash.com/photos/MJJ8p9b2AvY/download?ixid=M3w1Mzk1MTR8MHwxfGFsbHw2fHx8fHx8Mnx8MTcyMDgwNDg5MHw");
            Picture picture_5 = new Picture("https://images.unsplash.com/photo-1721023672487-906b07479bff?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHwyfHx8fHx8Mnx8MTcyMTExNTQzOXw&ixlib=rb-4.0.3&q=80&w=400", "A wooden table topped with a vase filled with flowers", "Parker Coffman", "lowmurmer", "https://unsplash.com/@lowmurmer", "https://api.unsplash.com/photos/jGDqRxc_xQ8/download?ixid=M3w1Mzk1MTR8MHwxfGFsbHwyfHx8fHx8Mnx8MTcyMTExNTQzOXw");
            Picture picture_6 = new Picture("https://images.unsplash.com/photo-1720887237185-6b8d5c01005d?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHw4fHx8fHx8Mnx8MTcyMTExNTQzOXw&ixlib=rb-4.0.3&q=80&w=400", "A couple of people that are looking in a window", "James Chan", "jvmesc_", "https://unsplash.com/@jvmesc_", "https://api.unsplash.com/photos/KWocLB1EHIc/download?ixid=M3w1Mzk1MTR8MHwxfGFsbHw4fHx8fHx8Mnx8MTcyMTExNTQzOXw");
            Picture picture_7 = new Picture("https://images.unsplash.com/photo-1722510192221-f380f5a1aeba?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHwxMHx8fHx8fDJ8fDE3MjMwOTgwNjZ8&ixlib=rb-4.0.3&q=80&w=400", "A woman leaning against a wall with her hands behind her head", "Hosein Sediqi", "hoseinsediqii", "https://unsplash.com/@hoseinsediqii", "https://api.unsplash.com/photos/2jbWnV-qniM/download?ixid=M3w1Mzk1MTR8MHwxfGFsbHwxMHx8fHx8fDJ8fDE3MjMwOTgwNjZ8");
            Picture picture_8 = new Picture("https://images.unsplash.com/photo-1722514474433-a6af73a6cb87?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHw4fHx8fHx8Mnx8MTcyMzA5ODA2Nnw&ixlib=rb-4.0.3&q=80&w=400", "A city street with a bunch of tall buildings", "Gabrielle Meschini", "gabriellemeschini", "https://unsplash.com/@gabriellemeschini", "https://api.unsplash.com/photos/3NfsacM7NrQ/download?ixid=M3w1Mzk1MTR8MHwxfGFsbHw4fHx8fHx8Mnx8MTcyMzA5ODA2Nnw");
            Picture picture_9 = new Picture("https://images.unsplash.com/photo-1722578601838-ee1439b17d50?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHw3fHx8fHx8Mnx8MTcyMzA5ODA2Nnw&ixlib=rb-4.0.3&q=80&w=400", "A couple of lawn chairs sitting on top of a beach", "Sergey Beschastnykh", "sergeyuxui", "https://unsplash.com/@sergeyuxui", "https://api.unsplash.com/photos/puHB5Lxh3l8/download?ixid=M3w1Mzk1MTR8MHwxfGFsbHw3fHx8fHx8Mnx8MTcyMzA5ODA2Nnw");
            Picture picture_10 = new Picture("https://images.unsplash.com/photo-1722082839802-18b18cb23a62?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHwzfHx8fHx8Mnx8MTcyMzA5ODA2Nnw&ixlib=rb-4.0.3&q=80&w=400", "A satellite image of a mountain range", "USGS", "usgs", "https://unsplash.com/@usgs", "https://api.unsplash.com/photos/P-a0aSW1A_M/download?ixid=M3w1Mzk1MTR8MHwxfGFsbHwzfHx8fHx8Mnx8MTcyMzA5ODA2Nnw");
            Picture picture_11 = new Picture("https://images.unsplash.com/photo-1719937050792-a6a15d899281?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MXwxfGFsbHw2fHx8fHx8Mnx8MTcyMzA5ODA2Nnw&ixlib=rb-4.0.3&q=80&w=400", "A man sitting at a table using a laptop computer", "Samsung Memory", "samsungmemory", "https://unsplash.com/@samsungmemory", "https://api.unsplash.com/photos/l_p4pay8CTM/download?ixid=M3w1Mzk1MTR8MXwxfGFsbHw2fHx8fHx8Mnx8MTcyMzA5ODA2Nnw");
            Picture picture_12 = new Picture("https://images.unsplash.com/photo-1722607571891-7ab5396a1b83?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1Mzk1MTR8MHwxfGFsbHw5fHx8fHx8Mnx8MTcyMzA5ODA2Nnw&ixlib=rb-4.0.3&q=80&w=400", "A bowl of yogurt with fruit on top", "Monika Grabkowska", "Monika Grabkowska", "https://unsplash.com/@moniqa", "https://api.unsplash.com/photos/g3F-FOlf51M/download?ixid=M3w1Mzk1MTR8MHwxfGFsbHw5fHx8fHx8Mnx8MTcyMzA5ODA2Nnw");

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");

            User user_1 = new User("test_user_1", "test_user_1");
            User user_2 = new User("test_user_2", "test_user_2");
            User admin = new User("test_admin", "test_admin");

            user_1.addRole(userRole);
            user_2.addRole(userRole);
            admin.addRole(adminRole);

            user_1.addPicture(picture_1);
            user_1.addPicture(picture_2);
            user_1.addPicture(picture_3);
            user_1.addPicture(picture_4);
            user_2.addPicture(picture_5);
            user_2.addPicture(picture_6);
            user_2.addPicture(picture_7);
            user_2.addPicture(picture_8);
            admin.addPicture(picture_9);
            admin.addPicture(picture_10);
            admin.addPicture(picture_11);
            admin.addPicture(picture_12);

            Rating rating_1 = new Rating(3, user_1);
            Rating rating_2 = new Rating(4, user_1);
            Rating rating_3 = new Rating(5, user_1);
            Rating rating_4 = new Rating(2, user_1);
            Rating rating_5 = new Rating(1, user_2);
            Rating rating_6 = new Rating(5, user_2);
            Rating rating_7 = new Rating(4, user_2);
            Rating rating_8 = new Rating(2, user_2);
            Rating rating_9 = new Rating(1, admin);
            Rating rating_10 = new Rating(3, admin);
            Rating rating_11 = new Rating(5, admin);
            Rating rating_12 = new Rating(3, admin);

            picture_1.addRating(rating_1);
            picture_2.addRating(rating_2);
            picture_3.addRating(rating_3);
            picture_4.addRating(rating_4);
            picture_5.addRating(rating_5);
            picture_6.addRating(rating_6);
            picture_7.addRating(rating_7);
            picture_8.addRating(rating_8);
            picture_9.addRating(rating_9);
            picture_10.addRating(rating_10);
            picture_11.addRating(rating_11);
            picture_12.addRating(rating_12);

            em.persist(rating_1);
            em.persist(rating_2);
            em.persist(rating_3);
            em.persist(rating_4);
            em.persist(rating_5);
            em.persist(rating_6);
            em.persist(rating_7);
            em.persist(rating_8);
            em.persist(rating_9);
            em.persist(rating_10);
            em.persist(rating_11);
            em.persist(rating_12);

            em.persist(user_1);
            em.persist(user_2);
            em.persist(admin);

            em.persist(userRole);
            em.persist(adminRole);

            em.persist(picture_1);
            em.persist(picture_2);
            em.persist(picture_3);
            em.persist(picture_4);
            em.persist(picture_5);
            em.persist(picture_6);
            em.persist(picture_7);
            em.persist(picture_8);
            em.persist(picture_9);
            em.persist(picture_10);
            em.persist(picture_11);
            em.persist(picture_12);

            em.getTransaction().commit();
        }
    }
}
