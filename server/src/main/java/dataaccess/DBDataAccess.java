package dataaccess;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.*;
import java.util.Collection;
import java.util.List;

public class DBDataAccess implements DataAccess {

    public DBDataAccess() {
        try {
            DatabaseManager.createDatabase();
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void submitConfig(String sql) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("failed to configure the database", e);
        }
    }
    private void configureDatabase() throws DataAccessException{
        String createUser = "CREATE TABLE IF NOT EXISTS UserData (username VARCHAR(50) PRIMARY KEY, password VARCHAR(50), email VARCHAR(50))";
        String createAuth = "CREATE TABLE IF NOT EXISTS AuthData (authToken VARCHAR(50) PRIMARY KEY, username VARCHAR(50))";
        String createGame = "CREATE TABLE IF NOT EXISTS GameData (gameID INT PRIMARY KEY, whiteUsername VARCHAR(50), blackUsername VARCHAR(50), gameName VARCHAR(50), game TEXT)";

        submitConfig(createUser);
        submitConfig(createAuth);
        submitConfig(createGame);
    }

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void createAuth(AuthData authToken) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(AuthData authToken) throws DataAccessException {

    }

    @Override
    public void createGame(GameData gameID) throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void updateGame(GameData gameID) throws DataAccessException {

    }
}
