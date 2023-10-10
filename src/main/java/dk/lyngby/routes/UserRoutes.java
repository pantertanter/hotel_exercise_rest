package dk.lyngby.routes;

import dk.lyngby.controller.impl.UserController;
import dk.lyngby.security.RouteRoles;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class UserRoutes {
    private final UserController userHandler = new UserController();

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/auth", () -> {
                post("/login", userHandler::login);
                post("/register", userHandler::register);
                get("/", userHandler::readAll, dk.lyngby.security.RouteRoles.ADMIN);
                get("{name}", userHandler::read, dk.lyngby.security.RouteRoles.ADMIN);
                put("{name}", userHandler::update, dk.lyngby.security.RouteRoles.ADMIN);
                delete("{name}", userHandler::delete, RouteRoles.ADMIN);
            });
        };
    }

}
