package dk.lyngby.dao.impl;

import dk.lyngby.dao.IDao;
import dk.lyngby.model.Picture;
import dk.lyngby.model.Rating;
import dk.lyngby.model.User;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class RatingDao implements IDao<Rating, Integer> {

    private static RatingDao instance;

    private static EntityManagerFactory emf;

    public static RatingDao getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new RatingDao();
        }
        return instance;
    }

    @Override
    public Rating read(Integer integer) {
        try (var em = emf.createEntityManager())
        {
            return em.find(Rating.class, integer);
        }
    }

    @Override
    public List<Rating> readAll() {
        try (var em = emf.createEntityManager())
        {
            var query = em.createQuery("SELECT r FROM Rating r", Rating.class);
            return query.getResultList();
        }
    }

    @Override
    public Rating create(Rating rating) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(rating);
            em.getTransaction().commit();
            return rating;
        }
    }

    @Override
    public Rating update(Integer integer, Rating rating) {
        try(var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            var r = em.find(Rating.class, integer);
            r.setRating(rating.getRating());
            em.getTransaction().commit();
            return r;
        }
    }

    @Override
    public void delete(Integer integer) {
        try(var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            var r = em.find(Rating.class, integer);
            em.remove(r);
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return false;
    }

    public Rating addRatingToPicture(int pictureId, Integer rating ) {
        try (var em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Picture picture = em.find(Picture.class, pictureId);
            Rating rating1 = new Rating(rating);
            picture.addRating(rating1);
            em.persist(rating);
            em.merge(picture);
            em.getTransaction().commit();
            return rating1;
        }
    }
}
