package dk.lyngby.controller.impl;

import dk.lyngby.exception.ApiException;
import dk.lyngby.exception.Message;
import dk.lyngby.routes.Routes;
import io.javalin.http.Context;
import io.javalin.validation.ValidationError;
import io.javalin.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExceptionController {
    private final Logger LOGGER = LoggerFactory.getLogger(Routes.class);

    public void validationExceptionHandler(ValidationException e , Context ctx) {
        LOGGER.error(ctx.attribute("requestInfo") + " " + ctx.res().getStatus() + " " + ctx.body());
        ctx.status(404);
        List<ValidationError<Object>> errors = e.getErrors().get("id");
        System.out.println(errors.get(0).getMessage());
        System.out.println(errors.get(0).getArgs());
        System.out.println(errors.get(0).getValue());
        ctx.json(e.getErrors());
    }

    public void apiExceptionHandler(ApiException e, Context ctx) {
        LOGGER.error(ctx.attribute("requestInfo") + " " + ctx.res().getStatus() + " " + e.getMessage());
        ctx.status(e.getStatusCode());
        ctx.json(new Message(e.getStatusCode(), e.getMessage()));
    }
    public void exceptionHandler(Exception e, Context ctx) {
        LOGGER.error(ctx.attribute("requestInfo") + " " + ctx.res().getStatus() + " " + e.getMessage());
        ctx.status(500);
        ctx.json(new Message(500, e.getMessage()));
    }

}
