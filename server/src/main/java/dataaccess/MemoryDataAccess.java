package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MemoryDataAccess implements DataAccess {
    HashMap<String, UserData> users = new HashMap<>();
    HashMap<String, AuthData> auths = new HashMap<>();
    HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        users.clear();
        auths.clear();
        games.clear();
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        users.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return users.get(username);
    }

    @Override
    public void createAuth(AuthData authToken) throws DataAccessException {
        auths.put(authToken.authToken(), authToken);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return auths.get(authToken);
    }

    @Override
    public void deleteAuth(AuthData authToken) throws DataAccessException {
        auths.remove(authToken.authToken());
    }

    @Override
    public void createGame(GameData gameID) throws DataAccessException {
        games.put(gameID.gameID(), gameID);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public void updateGame(GameData gameID) throws DataAccessException {
        games.put(gameID.gameID(), gameID);
    }
}
