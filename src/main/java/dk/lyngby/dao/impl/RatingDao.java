package dk.lyngby.dao.impl;

import dk.lyngby.dao.IDao;
import dk.lyngby.model.Picture;
import dk.lyngby.model.Rating;
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
            em.persist(rating1);
            em.merge(picture);
            em.getTransaction().commit();
            return rating1;
        }
    }

    public double getRatingsByPictureId(int pictureId) {
        try (var em = emf.createEntityManager()) {
            var query = em.createQuery("SELECT r FROM Rating r WHERE r.picture.id = :pictureId", Rating.class);
            query.setParameter("pictureId", pictureId);

            List<Rating> ratings = query.getResultList();
            if (ratings.isEmpty()) {
                return 0.0; // Return 0 if the list is empty to avoid division by zero
            }

            int sum = 0;
            for (Rating rating : ratings) {
                sum += rating.getRating(); // Assuming getRating() retrieves the rating value
            }

            return (double) sum / ratings.size(); // Calculate and return the average rating
        }
    }

}
