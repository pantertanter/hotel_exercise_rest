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
                post("/{picture_id}/{rating}", ratingController::addRatingToPicture, RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.MANAGER);
                get("/{picture_id}", ratingController::getRatingsByPictureId, RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.MANAGER);
//                delete("/{id}", ratingController::deleteRatingFromPicture, RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.MANAGER);

            });
        };
    }
}
