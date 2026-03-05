package server;

import service.GameService;
import io.javalin.http.Context;

public class GameHandler {
    private GameService listGames;
    private GameService createGame;
    private GameService joinGame;

    public GameHandler(GameService listGames, GameService createGame, GameService joinGame) {
        this.listGames = listGames;
        this.createGame = createGame;
        this.joinGame = joinGame;
    }

    public void listGames(Context response) {
        listGames.listGames();
        response.status(200);
        response.result(" { } ");
    }

    public void createGame(Context response) {
        listGames.createGame();
        response.status(200);
        response.result(" { } ");
    }

    public void joinGame(Context response) {
        listGames.joinGame();
        response.status(200);
        response.result(" { } ");
    }
}
