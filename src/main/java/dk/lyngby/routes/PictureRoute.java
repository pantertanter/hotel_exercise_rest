package dk.lyngby.routes;

import dk.lyngby.controller.impl.PictureController;
import dk.lyngby.security.RouteRoles;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class PictureRoute {

    private final PictureController pictureController = new PictureController();

        protected EndpointGroup getRoutes() {

            return () -> {
                path("/pictures", () -> {
                    post("/{userName}", pictureController::addPictureToUser, RouteRoles.USER, RouteRoles.ADMIN);
                    get("/{userName}", pictureController::readAllPicturesFromUser, RouteRoles.USER, RouteRoles.ADMIN);
                    delete("/{userName}", pictureController::deleteAllPicturesFromUser, RouteRoles.USER, RouteRoles.ADMIN);
                    get("/", pictureController::readAll, RouteRoles.ANYONE);
                    delete("/picture/{alt}/{userName}", pictureController::deletePictureFromUser, RouteRoles.USER, RouteRoles.ADMIN);
                });
            };
        }
    }

