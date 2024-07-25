package dk.lyngby.controller.impl;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.controller.IController;
import dk.lyngby.dao.impl.RatingDao;
import dk.lyngby.dto.RatingDto;
import dk.lyngby.model.Rating;
import dk.lyngby.model.User;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import io.javalin.http.Context;
import java.util.Collections;

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
        try {
            // Request parameters
            String picture_alt = ctx.pathParam("picture_alt");
            String ratingParam = ctx.pathParam("rating");
            String userNameForRating = ctx.pathParam("user_name_for_rating");

            // Convert rating to integer
            int ratingInteger;
            try {
                ratingInteger = Integer.parseInt(ratingParam);
            } catch (NumberFormatException e) {
                ctx.status(400);
                ctx.json("Invalid rating format. Must be an integer.");
                return;
            }

            // Check if the user has already rated the picture
            if (dao.getHasBeenRated(picture_alt, userNameForRating)) {
                ctx.status(409);
                ctx.json("User has already rated this image.");
            } else {
                // Add rating to picture
                Rating ratingResponse = dao.addRatingToPicture(picture_alt, ratingInteger, userNameForRating);

                // Create DTO
                RatingDto ratingDto = new RatingDto(ratingResponse);

                // Send response
                ctx.status(201);
                ctx.json(ratingDto);
            }
        } catch (Exception e) {
            ctx.status(500);
            ctx.json("An error occurred while adding the rating.");
            e.printStackTrace();
        }
    }

    public void getRatingByPictureId(Context ctx) {
        try {
            // Get picture_alt from path parameter
            String picture_alt = ctx.pathParam("picture_alt");

            // Get average ratings from DAO
            List<Double> averageRating = dao.getRatingsByPictureId(picture_alt);

            // Check if averageRating list is empty (no ratings found)
            if (averageRating.isEmpty()) {
                ctx.status(404); // Not Found
                ctx.json(Collections.singletonMap("error", "No ratings found for picture: " + picture_alt));
            } else {
                ctx.status(200); // OK
                ctx.json(averageRating);
            }
        } catch (Exception e) {
            ctx.status(500); // Server Error
            ctx.json(Collections.singletonMap("error", "Internal Server Error"));
            e.printStackTrace(); // Log the exception for debugging
        }
    }

    public void deleteRatingsFromUserPictures(Context ctx) {
        // request
        String userName = ctx.pathParam("userName");
        // entity
        dao.deleteRatingsFromUserPictures(userName);
        // response
        ctx.res().setStatus(204);
    }

    public void deleteRatingFromPicture(Context ctx) {
        // request
        String picture_alt = ctx.pathParam("picture_alt");
        String userName = ctx.pathParam("user_name");
        // entity
        dao.deleteRatingFromPicture(picture_alt, userName);
        // response
        ctx.res().setStatus(204);
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
