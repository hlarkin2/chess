package server;

import service.GameService;
import io.javalin.http.Context;

public class GameHandler {
    private GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void listGames(Context response) {
        gameService.listGames();
        response.status(200);
        response.result(" { } ");
    }

    public void createGame(Context response) {
        gameService.createGame();
        response.status(200);
        response.result(" { } ");
    }

    public void joinGame(Context response) {
        gameService.joinGame();
        response.status(200);
        response.result(" { } ");
    }
}
