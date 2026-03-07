package service;
import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;

import java.util.Collection;
import java.util.UUID;

public class GameService {
    private DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public Collection<GameData> listGames(AuthData auth) throws DataAccessException {
        if (dataAccess.getAuth(auth.authToken()) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        return dataAccess.listGames();
    }

    public int createGame(String gameName, AuthData auth) throws DataAccessException {
        if (dataAccess.getAuth(auth.authToken()) == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        if (gameName == null) {
            throw new DataAccessException("Error: bad request");
        }

        int gameID = Math.abs(UUID.randomUUID().hashCode());
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        dataAccess.createGame(newGame);
        return gameID;
    }

    public void joinGame(int gameID, AuthData authToken, String playerColor) throws DataAccessException {
        GameData selectGame = dataAccess.getGame(gameID);
        AuthData userToken = dataAccess.getAuth(authToken.authToken());

        if (userToken == null) {throw new DataAccessException("Error: unauthorized");}
        if (selectGame == null) {throw new DataAccessException("Error: bad request");}

        if (!"WHITE".equals(playerColor) && !"BLACK".equals(playerColor)) {
            throw new DataAccessException("Error: bad request");
        }

        if (playerColor.equals("WHITE") && selectGame.whiteUsername() != null) {
            throw new DataAccessException("Error: already taken");
        } else if (playerColor.equals("BLACK") && selectGame.blackUsername() != null) {
            throw new DataAccessException("Error: already taken");
        }

        if (playerColor.equals("WHITE")) {
            GameData newGame = new GameData(gameID, userToken.username(), selectGame.blackUsername(), selectGame.gameName(), selectGame.game());
            dataAccess.updateGame(newGame);
        } else if (playerColor.equals("BLACK")) {
            GameData newGame = new GameData(gameID, selectGame.whiteUsername(), userToken.username(), selectGame.gameName(), selectGame.game());
            dataAccess.updateGame(newGame);
        }
    }
}
