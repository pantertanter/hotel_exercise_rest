package dk.lyngby.routes;

import dk.lyngby.controller.impl.PictureController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PictureRoute {

    private final PictureController pictureController = new PictureController();

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/pictures", () -> {
                post("/", pictureController::create);
                get("/", pictureController::readAll);
                get("/{id}", pictureController::read);
                put("/{id}", pictureController::update);
                delete("/{id}", pictureController::delete);
            });
        };
    }
}
