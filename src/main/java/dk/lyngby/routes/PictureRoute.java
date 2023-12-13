package dk.lyngby.routes;

import dk.lyngby.controller.impl.HotelController;
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
                    post("/{userName}", pictureController::addPictureToUser, RouteRoles.ADMIN, RouteRoles.MANAGER);
                    get("/", pictureController::readAll, RouteRoles.ANYONE);
                    get("/{id}", pictureController::read, RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.MANAGER);
                    put("/{id}", pictureController::update, RouteRoles.ADMIN, RouteRoles.MANAGER);
                    delete("/{id}", pictureController::delete, RouteRoles.ADMIN, RouteRoles.MANAGER);
                });
            };
        }
    }

