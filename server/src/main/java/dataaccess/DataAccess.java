package dataaccess;
import model.UserData;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public interface DataAccess {
    void clear() throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void createAuth(AuthData authToken) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(AuthData authToken) throws DataAccessException;
    void createGame(GameData gameID) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    void updateGame(GameData gameID) throws DataAccessException;
}
