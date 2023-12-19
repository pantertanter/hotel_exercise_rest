package dk.lyngby.controller.impl;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.controller.IController;
import dk.lyngby.dao.impl.RatingDao;
import dk.lyngby.dto.HotelDto;
import dk.lyngby.dto.PictureDto;
import dk.lyngby.dto.RatingDto;
import dk.lyngby.model.Picture;
import dk.lyngby.model.Rating;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

import static java.lang.Integer.parseInt;

public class RatingController implements IController<Rating, Integer> {

    private final RatingDao dao;

    public RatingController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = RatingDao.getInstance(emf);
    }

    @Override
    public void read(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // entity
        Rating rating = dao.read(id);
        // dto
        RatingDto ratingDto = new RatingDto(rating);
        // response
        ctx.res().setStatus(200);
        ctx.json(ratingDto, RatingDto.class);
    }

    @Override
    public void readAll(Context ctx) {
        // entity
        List<Rating> ratings = dao.readAll();
        // dto
        List<RatingDto> ratingDtos = RatingDto.toRatingDTOList(ratings);
        // response
        ctx.res().setStatus(200);
        ctx.json(ratingDtos, RatingDto.class);
    }

    @Override
    public void create(Context ctx) {
        // request
        String userName = ctx.pathParam("userName");
        Rating jsonRequest = ctx.bodyAsClass(Rating.class);
        // entity
        Rating rating = dao.create(jsonRequest);
        // dto
        RatingDto ratingDto = new RatingDto(rating);
        // response
        ctx.res().setStatus(201);
        ctx.json(ratingDto, RatingDto.class);
    }

    @Override
    public void update(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // entity
        Rating jsonRequest = ctx.bodyAsClass(Rating.class);
        Rating rating = dao.update(id, jsonRequest);
        // dto
        RatingDto ratingDto = new RatingDto(rating);
        // response
        ctx.res().setStatus(200);
        ctx.json(ratingDto, RatingDto.class);
    }

    @Override
    public void delete(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // entity
        dao.delete(id);
        // response
        ctx.res().setStatus(204);
    }

    public void addRatingToPicture(Context ctx) {
        // request
        String picture_Id = ctx.pathParam("picture_id");
        String rating = ctx.pathParam("rating");
        int picture_id_int = parseInt(picture_Id);
        int rating_int = parseInt(rating);
        // entity
        Rating rating_response = dao.addRatingToPicture(picture_id_int, rating_int);
        // dto
        RatingDto ratingDto = new RatingDto(rating_response);
        // response
        ctx.res().setStatus(201);
        ctx.json(ratingDto, RatingDto.class);
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return dao.validatePrimaryKey(integer);
    }

    @Override
    public Rating validateEntity(Context ctx) {
        return ctx.bodyValidator(Rating.class)
                .check( r -> r.getRating() != null && !r.getRating().toString().isEmpty(), "Rating must be set")
                .get();
    }
}
