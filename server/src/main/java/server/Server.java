package server;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import io.javalin.*;
import service.ClearService;
import service.UserService;

public class Server {

    private final Javalin javalin;
    private DataAccess dataAccess;
    private ClearService clearService;
    private ClearHandler clearHandler;
    private UserService userService;
    private UserHandler userHandler;

    public Server() {
        dataAccess = new MemoryDataAccess();
        clearService = new ClearService(dataAccess);
        clearHandler = new ClearHandler(clearService);

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", clearHandler::clear);
        javalin.post("/user", userHandler::register);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
