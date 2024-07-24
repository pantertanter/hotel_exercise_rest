package dk.lyngby.dao.impl;

import dk.lyngby.dao.IDao;
import dk.lyngby.model.Picture;
import dk.lyngby.model.Rating;
import dk.lyngby.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
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
    public User update(User user) {
        return null;
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

    public Rating addRatingToPicture(String picture_alt, Integer rating, String userName) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            // find picture from picture_alt and userName
            Query query = em.createQuery(
                    "SELECT p FROM Picture p WHERE p.alt = :picture_alt AND p.user.username = :userName"
            );
            query.setParameter("picture_alt", picture_alt);
            query.setParameter("userName", userName);
            Picture picture = (Picture) query.getSingleResult();
//            Picture picture = em.find(Picture.class, picture_alt);
            User user = em.find(User.class, userName);
            Rating rating1 = new Rating(rating, picture, user);
            picture.addRating(rating1);
            em.persist(rating1);
            em.merge(picture);
            em.getTransaction().commit();
            return rating1;
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean getHasBeenRated(String picture_alt, String userName) {
        try (var em = emf.createEntityManager()) {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(r) FROM Rating r WHERE r.picture.alt = :picture_alt AND r.user.username = :userName",
                    Long.class
            );
            query.setParameter("picture_alt", picture_alt);
            query.setParameter("userName", userName);

            Long count = query.getSingleResult();
            return count > 0;
        } catch (NoResultException e) {
            return false; // Handle case where no results are found
        } catch (Exception e) {
            e.printStackTrace();
            // Handle other exceptions here
            return false;
        }
    }



    public List<Double> getRatingsByPictureId(String picture_alt) {
        try (var em = emf.createEntityManager()) {
            TypedQuery<Double> query = em.createQuery(
                    "SELECT AVG(r.rating) FROM Rating r WHERE r.picture.alt = :picture_alt GROUP BY r.picture.alt",
                    Double.class
            );
            query.setParameter("picture_alt", picture_alt);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // Handle exception gracefully
        }
    }



    public void deleteAllRatingsFromPicture(int pictureId) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Picture picture = em.find(Picture.class, pictureId);
            picture.getRatings().clear();
            em.getTransaction().commit();
        }
    }

    public void deleteRatingsFromUserPictures(String userName) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            int deletedCount = em.createQuery("DELETE FROM Rating r WHERE r.user.username = :userName")
                    .setParameter("userName", userName)
                    .executeUpdate();

            System.out.println("Deleted " + deletedCount + " ratings for user: " + userName);
            em.getTransaction().commit();
        } catch (Exception e) {
            // Handle exception, log error, rollback transaction if necessary
            e.printStackTrace();
        }
    }

}
