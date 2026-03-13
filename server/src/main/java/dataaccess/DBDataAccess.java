package dataaccess;
import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.*;
import java.util.ArrayList;
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
        String createUser = "CREATE TABLE IF NOT EXISTS UserData (username VARCHAR(50) PRIMARY KEY, password VARCHAR(60), email VARCHAR(50))";
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
        int gameid = gameID.gameID();
        String whiteUser = gameID.whiteUsername();
        String blackUser = gameID.blackUsername();
        String name = gameID.gameName();
        String game = new Gson().toJson(gameID.game());

        String insertGame = "INSERT INTO GameData (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?);";

        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(insertGame)) {
            preparedStatement.setInt(1, gameid);
            preparedStatement.setString(2, whiteUser);
            preparedStatement.setString(3, blackUser);
            preparedStatement.setString(4, name);
            preparedStatement.setString(5, game);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("error with creating game", e);
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String getGame = "SELECT * FROM GameData WHERE gameID = ?;";

        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(getGame)) {
            preparedStatement.setInt(1, gameID);
            try (var returns = preparedStatement.executeQuery()) {
                if (returns.next()) {
                    int returnedID = returns.getInt("gameID");
                    String returnedWhiteUser = returns.getString("whiteUsername");
                    String returnedBlackUser = returns.getString("blackUsername");
                    String returnedName = returns.getString("gameName");
                    ChessGame returnedGame = new Gson().fromJson(returns.getString("game"), ChessGame.class);
                    return new GameData(returnedID, returnedWhiteUser, returnedBlackUser, returnedName, returnedGame);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("error getting game", e);
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        String getGames = "SELECT * FROM GameData;";
        Collection<GameData> list = new ArrayList<>();

        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(getGames)) {
            try (var returns = preparedStatement.executeQuery()) {
                while (returns.next()) {
                    int returnedID = returns.getInt("gameID");
                    String returnedWhiteUser = returns.getString("whiteUsername");
                    String returnedBlackUser = returns.getString("blackUsername");
                    String returnedName = returns.getString("gameName");
                    ChessGame returnedGame = new Gson().fromJson(returns.getString("game"), ChessGame.class);
                    list.add(new GameData(returnedID, returnedWhiteUser, returnedBlackUser, returnedName, returnedGame));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("error getting token", e);
        }

        return list;
    }

    @Override
    public void updateGame(GameData gameID) throws DataAccessException {
        int gameid = gameID.gameID();
        String whiteUser = gameID.whiteUsername();
        String blackUser = gameID.blackUsername();
        String name = gameID.gameName();
        String game = new Gson().toJson(gameID.game());
        String update = "UPDATE GameData SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?;";

        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(update)) {
            preparedStatement.setString(1, whiteUser);
            preparedStatement.setString(2, blackUser);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, game);
            preparedStatement.setInt(5, gameid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("error getting game", e);
        }
    }
}
