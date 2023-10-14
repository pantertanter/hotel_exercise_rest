package dk.lyngby.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.lyngby.config.HibernateConfig;
import dk.lyngby.dao.AuthDao;
import dk.lyngby.dto.LoginDto;
import dk.lyngby.dto.RegisterDto;
import dk.lyngby.exception.ApiException;
import dk.lyngby.exception.AuthorizationException;
import dk.lyngby.model.User;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

public class AuthController {

    private final AuthDao dao;

    public AuthController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = AuthDao.getInstance(emf);
    }


    public void login(Context ctx) throws AuthorizationException {
        // TODO: validate login request and credentials
        LoginDto loginDto = ctx.bodyAsClass(LoginDto.class);
        User user = dao.verifyUser(loginDto.userName(), loginDto.userPassword());

        ctx.status(200);
        ctx.json(createResponseObject(user.getUserName(), "Here is your token"));
    }

    public void register(Context ctx) throws ApiException {

        // TODO: validate user
        RegisterDto registerDto = validateUser(ctx);
        // TODO: check if user exists
        dao.checkUser(registerDto.userName());
        // TODO: check if role exists
        dao.checkRoles(registerDto.roleList());
        // TODO: register user
        dao.registerUser(registerDto.userName(), registerDto.userPassword(), registerDto.roleList());

        ctx.status(201);
        ctx.json(createResponseObject(registerDto.userName(), "Here is your token"));
    }

    private ObjectNode createResponseObject(String userName, String token) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode respondNode = mapper.createObjectNode();
        respondNode.put("userName", userName);
        respondNode.put("token", token);
        return respondNode;
    }

    public RegisterDto validateUser(Context ctx) {
        return ctx.bodyValidator(RegisterDto.class)
                .check(user -> user.userName() != null && user.userPassword() != null, "Username and password must be set")
                .check(user -> user.userName().length() > 5, "Username must be at least 5 characters")
                .check(user -> user.userPassword().length() > 5, "Password must be at least 5 characters")
                .check(user -> !user.roleList().isEmpty(), "User must have at least one role")
                .get();
    }
}
