package dk.lyngby.dao.impl;

import dk.lyngby.model.Picture;
import jakarta.persistence.EntityManagerFactory;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PictureDao implements dk.lyngby.dao.IDao<Picture, Integer> {

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
        try (var em = emf.createEntityManager())
        {
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
            p.setPictureUrl(picture.getPictureUrl());
            Picture merge = em.merge(p);
            em.getTransaction().commit();
            return merge;
        }
    }

    @Override
    public void delete(Integer integer) {
        try(var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            var picture = em.find(Picture.class, integer);
            em.remove(picture);
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        try(var em = emf.createEntityManager()) {
            var person = em.find(Picture.class, integer);
            return person != null;
        }
    }
}
