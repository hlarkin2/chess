package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.UserService;
import io.javalin.http.Context;

import javax.xml.crypto.Data;
import java.util.Map;

public class UserHandler {
    private UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void register(Context response) {
        UserData newUser = new Gson().fromJson(response.body(), UserData.class);

        if (newUser.username() == null || newUser.password() == null || newUser.email() == null) {
            response.status(400);
            response.result(new Gson().toJson(Map.of("message", "Error: missing value(s)")));
            return;
        }

        try {
            AuthData authData = userService.register(newUser);
            response.status(200);
            response.result(new Gson().toJson(authData));
        } catch (DataAccessException e) {
            HandlerUtils.handleError(response, e);
        }
    }

    public void login(Context response) {
        UserData user = new Gson().fromJson(response.body(), UserData.class);

        if (user.username() == null || user.password() == null) {
            response.status(400);
            response.result(new Gson().toJson(Map.of("message", "Error: bad request")));
            return;
        }

        try {
            AuthData authData = userService.login(user);
            response.status(200);
            response.result(new Gson().toJson(authData));
        } catch (DataAccessException e) {
            HandlerUtils.handleError(response, e);
        }
    }

    public void logout(Context response) {
        AuthData auth = new AuthData(response.header("authorization"), null);

        try {
            userService.logout(auth);
            response.status(200);
            response.result("{}");
        } catch (DataAccessException e) {
            HandlerUtils.handleError(response, e);
        }
    }
}
