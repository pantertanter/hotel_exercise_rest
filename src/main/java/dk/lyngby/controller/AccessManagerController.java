package dk.lyngby.controller;

import dk.lyngby.exception.ApiException;
import dk.lyngby.exception.AuthorizationException;
import dk.lyngby.model.Role;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.security.RouteRole;

import java.util.Set;

public class AccessManagerController {

    public void accessManagerHandler(Handler handler, Context ctx, Set<? extends RouteRole> permittedRoles) throws Exception {

        String path = ctx.path();
        boolean isAuthorized = false;

        if (path.equals("/api/v1/routes") || permittedRoles.contains(Role.RoleName.ANYONE)) {
            handler.handle(ctx);
            return;
        } else {
            RouteRole[] roles = getRoles(ctx);
            for (RouteRole role : roles) {
                if (permittedRoles.contains(role)) {
                    isAuthorized = true;
                    break;
                }
            }
        }

        if (isAuthorized) {
            handler.handle(ctx);
        } else {
            throw new AuthorizationException(401, "You are not authorized to perform this action");
        }
    }

    private Role.RoleName[] getRoles(Context ctx) throws AuthorizationException, ApiException {

        // TODO: get user roles form user
        String token = ctx.header("Authorization").split(" ")[1];
        System.out.println(token);
        return new Role.RoleName[]{Role.RoleName.USER};
    }

}
