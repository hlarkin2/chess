package client;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.Collection;


public class ServerFacadeTests {
    private static ServerFacade facade;
    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade(port);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    void setup() throws ResponseException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void clearTest() throws ResponseException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        facade.register(user);
        facade.clear();
        Assertions.assertThrows(ResponseException.class, () -> facade.login(user));
    }

    @Test
    public void registerSuccessTest() throws ResponseException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        AuthData result = facade.register(user);
        Assertions.assertDoesNotThrow(() -> facade.login(user));
        Assertions.assertEquals(user.username(), result.username());
    }

    @Test
    public void registerFailTest() throws ResponseException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        facade.register(user);
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.register(user);
        });
    }

    @Test
    public void loginSuccessTest() throws ResponseException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        facade.register(user);
        AuthData result = facade.login(user);
                Assertions.assertEquals(user.username() ,result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void loginFailTest() throws ResponseException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.login(user);
        });
    }

    @Test
    public void logoutSuccessTest() throws ResponseException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        AuthData result = facade.register(user);
        Assertions.assertDoesNotThrow(() -> facade.logout(result));
    }

    @Test
    public void logoutFailTest() throws ResponseException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        AuthData result = facade.register(user);
        facade.logout(result);
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.logout(result);
        });
    }

    @Test
    public void listGamesSuccessTest() throws ResponseException {
        UserData user = new UserData("emily", "password", "emily@yahoo.com");
        AuthData result = facade.register(user);

        GameData game1 = new GameData(123, "white1", "black1", "firstGame", new ChessGame());
        GameData game2 = new GameData(124, "white2", "black2", "secondGame", new ChessGame());

        facade.createGame(game1, result);
        facade.createGame(game2, result);

        Collection<GameData> list = facade.listGames(result);
        Assertions.assertEquals(2, list.size());
    }

    @Test
    public void listGamesFailTest() throws ResponseException {
        AuthData token = new AuthData("fewnjgnwgvfsjk24thegnjs", "emily");
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.listGames(token);
        });
    }

    @Test
    public void createGameSuccessTest() throws ResponseException {
        UserData user = new UserData("emily", "password", "emily@yahoo.com");
        AuthData result = facade.register(user);

        GameData game1 = new GameData(123, "white1", "black1", "firstGame", new ChessGame());
        Assertions.assertDoesNotThrow(() -> facade.createGame(game1, result));
    }

    @Test
    public void createGameFailTest() throws ResponseException {
        AuthData token = new AuthData("fewnjgnwgvfsjk24thegnjs", "emily");
        GameData game1 = new GameData(123, "white1", "black1", "firstGame", new ChessGame());

        Assertions.assertThrows(ResponseException.class, () -> {
            facade.createGame(game1, token);
        });
    }

    @Test
    public void joinGameSuccessTest() throws ResponseException {
        UserData user = new UserData("emily", "password", "emily@yahoo.com");
        AuthData result = facade.register(user);
        String playerColor = "WHITE";
        GameData game = new GameData(123, "white1", "black1", "firstGame", new ChessGame());
        var response = facade.createGame(game, result);

        GameData game1 = new GameData(response, "white1", "black1", "firstGame", new ChessGame());

        Assertions.assertDoesNotThrow(() -> facade.joinGame(game1, playerColor, result));
    }

    @Test
    public void joinGameFailTest() throws ResponseException {
        UserData user = new UserData("emily", "password", "emily@yahoo.com");
        AuthData result = facade.register(user);
        String playerColor = "WHITE";
        GameData game1 = new GameData(123, "white1", "black1", "firstGame", new ChessGame());

        Assertions.assertThrows(ResponseException.class, () -> {
            facade.joinGame(game1, playerColor, result);
        });
    }
}
