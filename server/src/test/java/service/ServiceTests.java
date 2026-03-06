package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.Collection;

public class ServiceTests {
    DataAccess dataAccess;
    ClearService clearService;
    UserService userService;
    GameService gameService;

    @BeforeEach
    void pretest() {
        dataAccess = new MemoryDataAccess();
        clearService = new ClearService(dataAccess);
        userService = new UserService(dataAccess);
        gameService = new GameService(dataAccess);
    }

    @Test
    void clearSuccess() throws DataAccessException {
        UserData user = new UserData("emily", "password", "emily@yahoo.com");
        userService.register(user);
        clearService.clear();
        Assertions.assertNull(dataAccess.getUser(user.username()));
    }

    @Test
    void registerSuccess() throws DataAccessException {
        UserData user = new UserData("emily", "password", "emily@yahoo.com");
        AuthData result = userService.register(user);
        Assertions.assertEquals(dataAccess.getUser(user.username()), user);
        Assertions.assertNotNull(result.authToken());
        Assertions.assertNotNull(result.username());
    }

    @Test
    void registerFailure() throws DataAccessException {
        UserData user = new UserData("emily", "password", "emily@yahoo.com");
        userService.register(user);
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.register(user);
        });
    }

    @Test
    void loginSuccess() throws DataAccessException {
        UserData user = new UserData("emily", "password", "emily@yahoo.com");
        userService.register(user);
        AuthData result = userService.login(user);
        Assertions.assertNotNull(result.authToken());
        Assertions.assertEquals(result.username(), user.username());
    }

    @Test
    void loginFailure() throws DataAccessException {
        UserData user = new UserData("emily", "password", "emily@yahoo.com");
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.login(user);
        });
    }

    @Test
    void logoutSuccess() throws DataAccessException {
        UserData user = new UserData("emily", "password", "emily@yahoo.com");
        AuthData token = userService.register(user);
        userService.login(user);
        userService.logout(token);
        Assertions.assertNull(dataAccess.getAuth(token.authToken()));
    }

    @Test
    void logoutFailure() throws DataAccessException {
        AuthData token = new AuthData("abc123", "user");
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.logout(token);
        });
    }

    @Test
    void listGamesSuccess() throws DataAccessException {
        UserData user = new UserData("emily", "password", "emily@yahoo.com");
        AuthData token = userService.register(user);
        userService.login(user);
        GameData game1 = new GameData(123, "white1", "black1", "firstGame", new ChessGame());
        GameData game2 = new GameData(123, "white2", "black2", "secondGame", new ChessGame());

        gameService.createGame(game1.gameName(), token);
        gameService.createGame(game2.gameName(), token);

        Collection<GameData> list = gameService.listGames(token);
        Assertions.assertEquals(2, list.size());
    }

    @Test
    void listGamesFailure() throws DataAccessException {
        AuthData token = new AuthData("abc123", "user");

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.listGames(token);
        });
    }

    @Test
    void createGameSuccess() throws DataAccessException {
        UserData user = new UserData("emily", "password", "emily@yahoo.com");
        AuthData token = userService.register(user);
        userService.login(user);

        GameData game1 = new GameData(123, "white1", "black1", "firstGame", new ChessGame());
        int gameID = gameService.createGame(game1.gameName(), token);

        Assertions.assertNotNull(dataAccess.getGame(gameID));
    }

    @Test
    void createGameFailure() throws DataAccessException {
        AuthData token = new AuthData("abc123", "user");
        GameData game1 = new GameData(123, "white1", "black1", "firstGame", new ChessGame());

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.createGame(game1.gameName(), token);
        });
    }

    @Test
    void joinGameSuccess() throws DataAccessException {
        UserData user = new UserData("emily", "password", "emily@yahoo.com");
        AuthData token = userService.register(user);
        userService.login(user);
        GameData game = new GameData(123, "white1", "black1", "firstGame", new ChessGame());

        int gameID = gameService.createGame(game.gameName(), token);
        gameService.joinGame(gameID, token, "WHITE");

        Assertions.assertEquals(user.username(), dataAccess.getGame(gameID).whiteUsername());
    }

    @Test
    void joinGameFailure() throws DataAccessException {
        UserData user = new UserData("emily", "password", "emily@yahoo.com");
        AuthData token = userService.register(user);
        userService.login(user);
        GameData game = new GameData(123, "white1", "black1", "firstGame", new ChessGame());

        int gameID = gameService.createGame(game.gameName(), token);
        gameService.joinGame(gameID, token, "WHITE");

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(gameID, token, "WHITE");
        });
    }
}
