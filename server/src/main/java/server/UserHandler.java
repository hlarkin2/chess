package server;

import service.UserService;
import io.javalin.http.Context;

public class UserHandler {
    private UserService register;
    private UserService login;
    private UserService logout;

    public UserHandler(UserService register, UserService login, UserService logout) {
        this.register = register;
        this.login = login;
        this.logout = logout;
    }

    public void register(Context response) {
        register.register();
        response.status(200);
        response.result(" { } ");
    }

    public void login(Context response) {
        login.login();
        response.status(200);
        response.result(" { } ");
    }

    public void logout(Context response) {
        logout.logout();
        response.status(200);
        response.result(" { } ");
    }
}
