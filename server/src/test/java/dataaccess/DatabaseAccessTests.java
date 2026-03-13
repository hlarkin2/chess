package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class DatabaseAccessTests {
    DBDataAccess dbAccess;

    @BeforeEach
    void setup() throws DataAccessException {
        dbAccess = new DBDataAccess();
        dbAccess.clear();
    }

    @Test
    void clearSuccess() throws DataAccessException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        dbAccess.createUser(user);
        dbAccess.clear();
        Assertions.assertNull(dbAccess.getUser(user.username()));
    }

    @Test
    void createUserSuccess() throws DataAccessException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        dbAccess.createUser(user);
        UserData userName = dbAccess.getUser(user.username());
        Assertions.assertNotNull(userName);
        Assertions.assertEquals(user.username() ,userName.username());
    }

    @Test
    void createUserFail() throws DataAccessException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        dbAccess.createUser(user);
        Assertions.assertThrows(DataAccessException.class, () -> {
            dbAccess.createUser(user);
        });
    }

    @Test
    void getUserSuccess() throws DataAccessException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        dbAccess.createUser(user);
        UserData userName = dbAccess.getUser(user.username());
        Assertions.assertNotNull(userName);
        Assertions.assertEquals(user.username() ,userName.username());
    }

    @Test
    void getUserFail() throws DataAccessException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        Assertions.assertNull(dbAccess.getUser(user.username()));
    }

    @Test
    void createAuthSuccess() throws DataAccessException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        dbAccess.createUser(user);
        AuthData token = new AuthData("sgwgwh2onfw354sgfosg6r", user.username());
        dbAccess.createAuth(token);
        AuthData auth = dbAccess.getAuth(token.authToken());
        Assertions.assertNotNull(auth);
    }

    @Test
    void createAuthFail() throws DataAccessException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        dbAccess.createUser(user);
        AuthData token = new AuthData("sgwgwh2onfw354sgfosg6r", user.username());
        dbAccess.createAuth(token);
        Assertions.assertThrows(DataAccessException.class, () -> {
            dbAccess.createAuth(token);
        });
    }

    @Test
    void getAuthSuccess() throws DataAccessException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        dbAccess.createUser(user);
        AuthData token = new AuthData("sgwgwh2onfw354sgfosg6r", user.username());
        dbAccess.createAuth(token);
        AuthData auth = dbAccess.getAuth(token.authToken());
        Assertions.assertNotNull(auth);
    }

    @Test
    void getAuthFail() throws DataAccessException {
        AuthData auth = dbAccess.getAuth("ghifankfsnkogr823rthegfn");
        Assertions.assertNull(auth);
    }

    @Test
    void deleteAuthSuccess() throws DataAccessException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        dbAccess.createUser(user);
        AuthData token = new AuthData("sgwgwh2onfw354sgfosg6r", user.username());
        dbAccess.createAuth(token);
        dbAccess.deleteAuth(token);
        Assertions.assertNull(dbAccess.getAuth(token.authToken()));
    }

    @Test
    void deleteAuthFail() throws DataAccessException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        dbAccess.createUser(user);
        AuthData token = new AuthData("sgwgwh2onfw354sgfosg6r", user.username());
        dbAccess.createAuth(token);
        dbAccess.deleteAuth(new AuthData("gnwjgw4j2t", "amy"));
        Assertions.assertNotNull(dbAccess.getAuth(token.authToken()));
    }

    @Test
    void createGameSuccess() throws DataAccessException {
        UserData user = new UserData("emily", "password", "emily@yahoo.com");
        dbAccess.createUser(user);

        GameData game1 = new GameData(123, "white1", "black1", "firstGame", new ChessGame());
        dbAccess.createGame(game1);

        Assertions.assertNotNull(dbAccess.getGame(game1.gameID()));
    }

    @Test
    void createGameFail() throws DataAccessException {
        GameData game1 = new GameData(123, "white1", "black1", "firstGame", new ChessGame());
        GameData game2 = new GameData(123, "white1", "black1", "firstGame", new ChessGame());
        dbAccess.createGame(game1);

        Assertions.assertThrows(DataAccessException.class, () -> {
            dbAccess.createGame(game2);
        });
    }

    @Test
    void getGameSuccess() throws DataAccessException {
        GameData game1 = new GameData(123, "white1", "black1", "firstGame", new ChessGame());
        dbAccess.createGame(game1);
        GameData game = dbAccess.getGame(game1.gameID());
        Assertions.assertNotNull(game);
    }

    @Test
    void getGameFail() throws DataAccessException {
        GameData game = dbAccess.getGame(123);
        Assertions.assertNull(game);
    }

    @Test
    void listGamesSuccess() throws DataAccessException {
        GameData game1 = new GameData(123, "white1", "black1", "firstGame", new ChessGame());
        GameData game2 = new GameData(124, "white2", "black2", "secondGame", new ChessGame());

        dbAccess.createGame(game1);
        dbAccess.createGame(game2);

        Collection<GameData> list = dbAccess.listGames();
        Assertions.assertEquals(2, list.size());
    }

    @Test
    void listGamesFail() throws DataAccessException {
        Collection<GameData> list = dbAccess.listGames();
        Assertions.assertEquals(0, list.size());
    }

    @Test
    void updateGameSuccess() throws DataAccessException {
        GameData game1 = new GameData(123, "white1", "black1", "firstGame", new ChessGame());
        dbAccess.createGame(game1);

        GameData updated = new GameData(123, "newUser", "black1", "secondGame", new ChessGame());
        dbAccess.updateGame(updated);
        GameData updatedGame = dbAccess.getGame(123);
        Assertions.assertEquals(updated.blackUsername(), updatedGame.blackUsername());
    }

    @Test
    void updateGameFail() throws DataAccessException {
        GameData game1 = new GameData(123, "white1", "black1", "firstGame", new ChessGame());
        dbAccess.updateGame(game1);
        Assertions.assertNull(dbAccess.getGame(game1.gameID()));
    }

}
