package server;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import io.javalin.*;
import service.ClearService;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;
    private DataAccess dataAccess;
    private ClearService clearService;
    private ClearHandler clearHandler;
    private UserService userService;
    private UserHandler userHandler;
    private GameService gameService;
    private GameHandler gameHandler;

    public Server() {
        dataAccess = new MemoryDataAccess();
        clearService = new ClearService(dataAccess);
        clearHandler = new ClearHandler(clearService);
        userService = new UserService(dataAccess);
        userHandler = new UserHandler(userService);
        gameService = new GameService(dataAccess);
        gameHandler = new GameHandler(gameService);

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", clearHandler::clear);
        javalin.post("/user", userHandler::register);
        javalin.post("/session", userHandler::login);
        javalin.delete("/session", userHandler::logout);
        javalin.get("/game", gameHandler::listGames);
        javalin.post("/game", gameHandler::createGame);
        javalin.put("/game", gameHandler::joinGame);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
