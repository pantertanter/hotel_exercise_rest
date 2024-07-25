package dk.lyngby.routes;

import dk.lyngby.controller.impl.RatingController;
import dk.lyngby.security.RouteRoles;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;
public class RatingRoute {

    private final RatingController ratingController = new RatingController();

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/ratings", () -> {
                post("/{picture_alt}/{rating}/{user_name_for_rating}", ratingController::addRatingToPicture, RouteRoles.USER, RouteRoles.ADMIN);
                get("/{picture_alt}", ratingController::getRatingByPictureId, RouteRoles.USER, RouteRoles.ADMIN);
                delete("/{userName}", ratingController::deleteRatingsFromUserPictures, RouteRoles.ADMIN);
                delete("/rating/{picture_alt}/{user_name}", ratingController::deleteRatingFromPicture, RouteRoles.ADMIN);

            });
        };
    }
}
