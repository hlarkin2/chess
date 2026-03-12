package dataaccess;
import java.sql.*;

public class DBDataAccess implements DataAccess {

    public DBDataAccess() {
        DatabaseManager.createDatabase();
    }

    private void configureDatabase() {
        String createUser = "CREATE TABLE IF NOT EXISTS UserData (username VARCHAR(50) PRIMARY KEY, password VARCHAR(50), email VARCHAR(50))";
        String createAuth = "CREATE TABLE IF NOT EXISTS AuthData (authToken VARCHAR(50) PRIMARY KEY, username VARCHAR(50))";
        String createGame = "CREATE TABLE IF NOT EXISTS GameData (gameID INT PRIMARY KEY, whiteUsername VARCHAR(50), blackUsername VARCHAR(50), gameName VARCHAR(50), game TEXT)";
    }
}
