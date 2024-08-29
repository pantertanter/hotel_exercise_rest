package dk.lyngby.routes;

import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final PictureRoute pictureRoute = new PictureRoute();

    public EndpointGroup getRoutes(Javalin app) {
        return () -> {
            app.routes(() -> {
                path("/", pictureRoute.getRoutes());
            });
        };
    }
}
