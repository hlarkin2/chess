package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import service.GameService;
import io.javalin.http.Context;
import service.JoinRequest;

import java.util.Collection;
import java.util.Map;

public class GameHandler {
    private GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void listGames(Context response) {
        AuthData auth = new AuthData(response.header("authorization"), null);

        try {
            Collection<GameData> list = gameService.listGames(auth);
            response.status(200);
            response.result(new Gson().toJson(Map.of("games", list)));
        } catch (DataAccessException e) {
            HandlerUtils.handleError(response, e);
        }
    }

    public void createGame(Context response) {
        Map body = new Gson().fromJson(response.body(), Map.class);
        String gameName = (String) body.get("gameName");
        AuthData auth = new AuthData(response.header("authorization"), null);

        try {
            int gameID = gameService.createGame(gameName, auth);
            response.status(200);
            response.result(new Gson().toJson(Map.of("gameID", gameID)));
        } catch (DataAccessException e) {
            HandlerUtils.handleError(response, e);
        }
    }

    public void joinGame(Context response) {
        AuthData auth = new AuthData(response.header("authorization"), null);
        JoinRequest join = new Gson().fromJson(response.body(), JoinRequest.class);

        try {
            gameService.joinGame(join.gameID(), auth, join.playerColor());
            response.status(200);
            response.result("{}");
        } catch (DataAccessException e) {
            HandlerUtils.handleError(response, e);
        }
    }
}
