package dk.lyngby.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.lyngby.config.HibernateConfig;
import dk.lyngby.controller.IController;
import dk.lyngby.dao.impl.UserDao;
import dk.lyngby.dto.UserDTO;
import dk.lyngby.exception.ApiException;
import dk.lyngby.exception.AuthorizationException;
import dk.lyngby.exception.Message;
import dk.lyngby.model.User;
import dk.lyngby.security.TokenFactory;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Set;

public class UserController implements IController<UserDTO, String> {

    private final UserDao userDao;
    private final TokenFactory tokenFactory = TokenFactory.getInstance();

    public UserController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        userDao = UserDao.getInstance(emf);
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

    @Override
    public void read(Context ctx) throws ApiException {
        // request
        String userName = ctx.pathParamAsClass("name", String.class).check(this::validatePrimaryKey, "Not a valid name").get();
        // entity
        User user = userDao.read(userName);
        // dto
        UserDTO userDTO = new UserDTO(user);
        // response
        ctx.res().setStatus(200);
        ctx.json(userDTO, UserDTO.class);
    }

    @Override
    public void readAll(Context ctx) {
        // entity
        List<User> users = userDao.readAll();
        // dto
        List<UserDTO> userDTOS = UserDTO.toUserDTOList(users);
        // response
        ctx.status(200);
        ctx.json(userDTOS, UserDTO.class);
    }

    @Override
    public void create(Context ctx) {
        ctx.result("Use register instead");
    }

    @Override
    public void update(Context ctx) {
        ctx.result("Not implemented");
    }

    @Override
    public void delete(Context ctx) throws ApiException {
        // request
        String userName = ctx.pathParamAsClass("name", String.class).check(this::validatePrimaryKey, "Not a valid name").get();
        // entity
        userDao.delete(userName);
        // response
        ctx.res().setStatus(200);
        ctx.json(new Message(200, "User with name " + userName + " deleted"), Message.class);
    }

    @Override
    public boolean validatePrimaryKey(String userName) {
        return userDao.validatePrimaryKey(userName);
    }

    @Override
    public UserDTO validateEntity(Context ctx) {
        return null;
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

    private String getToken(String username, Set<String> userRoles) throws ApiException {
        return tokenFactory.createToken(username, userRoles);
    }
}
