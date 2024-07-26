package dk.lyngby.controller.impl;

import dk.lyngby.dto.UserDto;
import dk.lyngby.exception.ApiException;
import dk.lyngby.exception.AuthorizationException;
import dk.lyngby.security.RouteRoles;
import dk.lyngby.security.TokenFactory;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.security.RouteRole;

import java.util.Set;

public class AccessManagerController
{

    private final TokenFactory TOKEN_FACTORY = TokenFactory.getInstance();

    public void accessManagerHandler(Handler handler, Context ctx, Set<? extends RouteRole> permittedRoles) throws Exception
    {
        String path = ctx.path();
        boolean isAuthorized = false;

        if (path.equals("/api/v1/auth/login") ||
                path.equals("/api/v1/auth/register") ||
                path.equals("/api/v1/routes") ||
                permittedRoles.contains(RouteRoles.ANYONE) ||
                ctx.method().toString().equals("OPTIONS")
        )
        {
            handler.handle(ctx);
            return;
        } else
        {
            RouteRole[] userRole = getUserRole(ctx);
            for (RouteRole role : userRole)
            {
                if (permittedRoles.contains(role))
                {
                    isAuthorized = true;
                    break;
                }
            }
        }

        if (isAuthorized)
        {
            handler.handle(ctx);
        } else
        {
            throw new AuthorizationException(401, "You are not authorized to perform this action");
        }
    }

    private RouteRole[] getUserRole(Context ctx) throws AuthorizationException, ApiException {
        try {
            String authHeader = ctx.header("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ApiException(401, "Missing or malformed authorization header");
            }

            String[] parts = authHeader.split(" ");
            if (parts.length != 2) {
                throw new ApiException(401, "Invalid authorization header format");
            }

            String token = parts[1];
            UserDto userDTO = TOKEN_FACTORY.verifyToken(token);

            if (userDTO.getRoles() == null || userDTO.getRoles().isEmpty()) {
                throw new ApiException(403, "User has no roles assigned");
            }

            // Log the token and roles for debugging
            System.out.println("Token: " + token);
            System.out.println("User roles: " + userDTO.getRoles());

            return userDTO.getRoles().stream()
                    .map(r -> RouteRoles.valueOf(r.toUpperCase()))
                    .toArray(RouteRole[]::new);
        } catch (NullPointerException e) {
            throw new ApiException(401, "Invalid token");
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid role in token");
        }
    }
}