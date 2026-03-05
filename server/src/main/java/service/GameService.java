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

    public Collection<GameData> listGames() throws DataAccessException {
        return dataAccess.listGames();
    }

    public int createGame(String gameName) throws DataAccessException {
        int gameID = UUID.randomUUID().hashCode();
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        dataAccess.createGame(newGame);
        return gameID;
    }

    public GameData joinGame(int gameID, AuthData authToken, String playerColor) throws DataAccessException {
        GameData selectGame = dataAccess.getGame(gameID);
        AuthData userToken = dataAccess.getAuth(authToken.authToken());


    }
}
