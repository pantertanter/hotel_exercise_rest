package dk.lyngby.routes;

import dk.lyngby.controller.impl.UserController;
import dk.lyngby.security.RouteRoles;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UserRoutes {
    private final UserController userController = new UserController();

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/auth", () -> {
                post("/login", userController::login, RouteRoles.ANYONE);
                post("/register", userController::register, RouteRoles.ANYONE);
                get("/getAllUsers", userController::getAllUsers, RouteRoles.ADMIN);
            });
        };
    }
}