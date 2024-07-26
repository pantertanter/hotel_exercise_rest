package dk.lyngby.controller.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.lyngby.config.HibernateConfig;
import dk.lyngby.dao.impl.PictureDao;
import dk.lyngby.dao.impl.UserDao;
import dk.lyngby.exception.ApiException;
import dk.lyngby.exception.AuthorizationException;
import dk.lyngby.model.Picture;
import dk.lyngby.model.Role;
import dk.lyngby.model.User;
import dk.lyngby.security.TokenFactory;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserController {

    private final UserDao userDao;

    private final PictureDao pictureDao;

    private final TokenFactory tokenFactory = TokenFactory.getInstance();

    public UserController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        userDao = UserDao.getInstance(emf);
        pictureDao = PictureDao.getInstance(emf);
    }

    public void login(Context ctx) throws ApiException, AuthorizationException {
        String[] userInfos = getUserInfos(ctx, true);
        User user = getVerfiedOrRegisterUser(userInfos[0], userInfos[1], "", false);
        String token = getToken(userInfos[0], user.getRolesAsStrings());

        // Create response
        ctx.status(200);
        ctx.result(createResponse(userInfos[0], token));
    }

    public void register(Context ctx) throws ApiException, AuthorizationException {
        String[] userInfos = getUserInfos(ctx, false);
        User user = getVerfiedOrRegisterUser(userInfos[0], userInfos[1], userInfos[2], true);
        String token = getToken(userInfos[0], user.getRolesAsStrings());

        // Create response
        ctx.res().setStatus(201);
        ctx.result(createResponse(userInfos[0], token));
    }

    private String createResponse(String username, String token) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseJson = mapper.createObjectNode();
        responseJson.put("username", username);
        responseJson.put("token", token);
        return responseJson.toString();
    }

    private String[] getUserInfos(Context ctx, boolean tryLogin) throws ApiException {
        String request = ctx.body();
        return tokenFactory.parseJsonObject(request, tryLogin);
    }

    private User getVerfiedOrRegisterUser(String username, String password, String role, boolean isCreate) throws AuthorizationException {
        return isCreate ? userDao.registerUser(username, password, role) : userDao.getVerifiedUser(username, password);
    }

    public String getToken(String username, Set<String> userRoles) throws ApiException {
        return tokenFactory.createToken(username, userRoles);
    }

    private User findUserByName(String username) {
        return userDao.read(username);
    }

    public void getAllUsers(Context ctx) {
        // get all users and display a status 200 ok if successful else display a status 500 internal server error
        List<User> users = userDao.getAllUsers();
        ctx.status(200);
        ctx.json(users);
    }

    public void delete(Context ctx) {
        // get the id of the user to be deleted

        String id = ctx.pathParam("id");
        // delete the user and display a status 200 ok if successful else display a status 500 internal server error
        pictureDao.deleteAllPicturesFromUser(id);
        userDao.delete(id);
        ctx.status(200);
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    public static class RoleUpdateRequest {
        private Set<String> roles;

        public Set<String> getRoles() {
            return roles;
        }

        public void setRoles(Set<String> roles) {
            this.roles = roles;
        }
    }

    public void update(Context ctx) {
        try {
            String userName = ctx.pathParam("user_name");

            // Read JSON payload from request body
            String jsonPayload = ctx.body();
            System.out.println("JSON Payload: " + jsonPayload);

            // Deserialize JSON payload into RoleUpdateRequest
            RoleUpdateRequest roleUpdateRequest = objectMapper.readValue(jsonPayload, RoleUpdateRequest.class);
            System.out.println("Deserialized RoleUpdateRequest: " + roleUpdateRequest);

            // Convert role names to Set<Role>
            Set<Role> rolesSet = new HashSet<>();
            for (String roleName : roleUpdateRequest.getRoles()) {
                rolesSet.add(new Role(roleName));
            }
            System.out.println("Converted Roles Set:" + " " + rolesSet);

                    // Find user by username
                    User user = userDao.findUserByName(userName);

            if (user != null) {
                // Update user roles
                user.getRoleList().clear(); // Clear existing roles if needed
                user.getRoleList().addAll(rolesSet); // Add new roles

                // Persist updated user
                userDao.update(user);

                // Respond with updated user
                ctx.status(200);
                ctx.json(user);
            } else {
                ctx.status(404);
                ctx.result("User not found");
            }
        } catch (Exception e) {
            ctx.status(500);
            ctx.result("Failed to update user roles");
            e.printStackTrace();
        }
    }
}