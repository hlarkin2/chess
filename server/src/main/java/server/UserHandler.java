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
            if (e.getMessage().contains("already taken")) {
                response.status(403);
            } else if (e.getMessage().contains("unauthorized")) {
                response.status(401);
            } else {
                response.status(500);
            }
            response.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }

    public void login(Context response) {
        UserData user = new Gson().fromJson(response.body(), UserData.class);

        try {
            AuthData authData = userService.login(user);
            response.status(200);
            response.result(new Gson().toJson(authData));
        } catch (DataAccessException e) {
            if (e.getMessage().contains("bad request")) {
                response.status(400);
            } else if (e.getMessage().contains("unauthorized")) {
                response.status(401);
            } else {
                response.status(500);
            }
            response.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }

    public void logout(Context response) {
        AuthData auth = new AuthData(response.header("authorization"), null);

        try {
            userService.logout(auth);
            response.status(200);
            response.result("{}");
        } catch (DataAccessException e) {
            if (e.getMessage().contains("unauthorized")) {
                response.status(401);
            } else {
                response.status(500);
            }
            response.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }
}
