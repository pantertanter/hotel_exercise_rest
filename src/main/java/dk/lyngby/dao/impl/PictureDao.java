package dk.lyngby.dao.impl;

import dk.lyngby.dao.IDao;
import dk.lyngby.model.Picture;
import dk.lyngby.model.Rating;
import dk.lyngby.model.User;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PictureDao implements IDao<Picture, Integer> {

    private static PictureDao instance;

    private static EntityManagerFactory emf;

    public static PictureDao getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PictureDao();
        }
        return instance;
    }
    @Override
    public Picture read(Integer integer) {
        try (var em = emf.createEntityManager())
        {
            return em.find(Picture.class, integer);
        }
    }

    @Override
    public List<Picture> readAll() {
        try (var em = emf.createEntityManager())
        {
            var query = em.createQuery("SELECT p FROM Picture p", Picture.class);
            return query.getResultList();
        }
    }

    @Override
    public Picture create(Picture picture) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(picture);
            em.getTransaction().commit();
            return picture;
        }
    }

    @Override
    public Picture update(Integer integer, Picture picture) {
        try(var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            var p = em.find(Picture.class, integer);
            p.setUrl(picture.getUrl());
            p.setAlt(picture.getAlt());

            em.getTransaction().commit();
            return p;
        }
    }

    @Override
    public void delete(Integer integer) {
        try(var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            var p = em.find(Picture.class, integer);
            em.remove(p);
            em.getTransaction().commit();
        }
    }

    public Picture addPictureToUser(String userName, Picture picture ) {
        try (var em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            User user = em.find(User.class, userName);
            user.addPicture(picture);
            em.persist(picture);
            em.merge(user);
            em.getTransaction().commit();
            return picture;
        }
    }

    public List<Picture> readAllPicturesFromUser(String userName) {
        try (var em = emf.createEntityManager())
        {
            var query = em.createQuery("SELECT p FROM Picture p WHERE p.user.username = :userName", Picture.class);
            query.setParameter("userName", userName);
            return query.getResultList();
        }
    }

    public void deleteAllPicturesFromUser(String userName) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Corrected JPQL query with parameter binding
            int deletedCount = em.createQuery("DELETE FROM Picture p WHERE p.user.username = :userName")
                    .setParameter("userName", userName)
                    .executeUpdate();

            System.out.println("Deleted " + deletedCount + " pictures for user: " + userName);

            em.getTransaction().commit();
        } catch (Exception e) {
            // Handle exception, log error, rollback transaction if necessary
            e.printStackTrace();
        }
    }

    public Picture deletePictureFromUser(Integer pictureId) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find the picture
            Picture picture = em.find(Picture.class, pictureId);

            if (picture != null) {
                // Remove ratings associated with the picture
                var removeRatingsQuery = em.createQuery("DELETE FROM Rating r WHERE r.picture = :picture");
                removeRatingsQuery.setParameter("picture", picture);
                removeRatingsQuery.executeUpdate();

                // Remove picture from user's pictures
                User user = picture.getUser(); // Assuming Picture has a reference to the User
                if (user != null) {
                    user.getPictures().remove(picture);
                }

                // Remove picture entity
                em.remove(picture);
                em.getTransaction().commit();
            }

            return picture;
        }
    }



    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return false;
    }
}
