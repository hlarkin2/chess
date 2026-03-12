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
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
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
        String deleteGame = "DELETE FROM GameData;";
        String deleteAuth = "DELETE FROM AuthData;";
        String deleteUser = "DELETE FROM UserData;";

        submitConfig(deleteGame);
        submitConfig(deleteAuth);
        submitConfig(deleteUser);
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        String username = user.username();
        String password = user.password();
        String email = user.email();

        String salt = BCrypt.gensalt(12);
        String hashedPass = BCrypt.hashpw(password, salt);

        String insertUser = "INSERT INTO UserData (username, password, email) VALUES (?, ?, ?);";

        try (var conn = DatabaseManager.getConnection();
            var preparedStatement = conn.prepareStatement(insertUser)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPass);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("error with creating user", e);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String getUser = "SELECT username, password, email FROM UserData WHERE username = ?;";

        try (var conn = DatabaseManager.getConnection();
            var preparedStatement = conn.prepareStatement(getUser)) {
            preparedStatement.setString(1, username);
            try (var returns = preparedStatement.executeQuery()) {
                if (returns.next()) {
                    String returnedUsername = returns.getString("username");
                    String returnedPassword = returns.getString("password");
                    String returnedEmail = returns.getString("email");
                    return new UserData(returnedUsername, returnedPassword, returnedEmail);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("error getting user", e);
        }
        return null;
    }

    @Override
    public void createAuth(AuthData authToken) throws DataAccessException {
        String username = authToken.username();
        String insertAuth = "INSERT INTO AuthData (authToken, username) VALUES (?, ?);";

        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(insertAuth)) {
            preparedStatement.setString(1, authToken.authToken());
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("error with creating authToken", e);
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        String getAuth = "SELECT authToken, username FROM AuthData WHERE authToken = ?;";

        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(getAuth)) {
            preparedStatement.setString(1, authToken);
            try (var returns = preparedStatement.executeQuery()) {
                if (returns.next()) {
                    String returnedToken = returns.getString("authToken");
                    String returnedUsername = returns.getString("username");
                    return new AuthData(returnedToken, returnedUsername);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("error getting token", e);
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData authToken) throws DataAccessException {
        String token = authToken.authToken();
        String delAuth = "DELETE FROM AuthData WHERE authToken = ?;";

        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(delAuth)) {
            preparedStatement.setString(1, token);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("error getting token", e);
        }
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
