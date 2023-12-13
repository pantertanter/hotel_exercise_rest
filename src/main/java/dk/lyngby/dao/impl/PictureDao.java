package dk.lyngby.dao.impl;

import dk.lyngby.dao.IDao;
import dk.lyngby.model.Picture;
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

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return false;
    }
}
