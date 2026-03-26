package client;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ChessClient {
    private String username = null;
    private String authToken = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private List<GameData> gameList = new ArrayList<>();

    public ChessClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String command = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (command) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> quit();
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "play" -> playGame(params);
                case "observe" -> observeGame(params);
                case "logout" -> logout();
                default -> help();
            };
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            UserData user = new UserData(params[0], params[1], null);
            AuthData token = server.login(user);
            authToken = token.authToken();
            state = State.SIGNEDIN;
            username = params[0];
            return String.format("Signed in as %s", username);
        }
        throw new ResponseException("Expected: <username>");
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            UserData user = new UserData(params[0], params[1], params[2]);
            AuthData token = server.register(user);
            authToken = token.authToken();
            state = State.SIGNEDIN;
            username = params[0];
            return String.format("Created new user: %s", username);
        }
        throw new ResponseException("Error with registering new user.");
    }

    public String quit() {
        return "quit";
    }

    public String logout(String... params) throws ResponseException {
        assertLoggedIn();
        AuthData auth = new AuthData(authToken, username);
        server.logout(auth);
        authToken = null;
        username = null;
        state = State.SIGNEDOUT;
        return String.format("Logged out");
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            assertLoggedIn();
            AuthData auth = new AuthData(authToken, username);
            GameData game = new GameData(0, null, null, params[0], null);
            server.createGame(game, auth);
            return String.format("Created new game: %s", params[0]);
        }
        throw new ResponseException("Error with creating new game.");
    }

    public String listGames(String... params) throws ResponseException {
        assertLoggedIn();
        AuthData auth = new AuthData(authToken, username);
        Collection<GameData> games = server.listGames(auth);
        gameList = new ArrayList<>(games);
        StringBuilder result = new StringBuilder();
        for (int num = 0; num < gameList.size(); num++) {
            GameData game = gameList.get(num);
            result.append(String.format("%d. %s | White: %s | Black: %s\n", num + 1, game.gameName(), game.whiteUsername(), game.blackUsername()));
        }
        return result.toString();
    }

    public String playGame(String... params) throws ResponseException {
        if (params.length >= 2) {
            assertLoggedIn();
            AuthData auth = new AuthData(authToken, username);
            int gameNum = Integer.parseInt(params[0]);
            try {
                GameData game = gameList.get(gameNum - 1);
                server.joinGame(game, params[1].toUpperCase(), auth);
                ChessGame.TeamColor color = params[1].equalsIgnoreCase("BLACK") ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
                new BoardRenderer(game.game().getBoard(), color).render();
                return String.format("Joining game: %s", params[0]);
            } catch (IndexOutOfBoundsException e) {
                throw new ResponseException("Error: Invalid game number.");
            } catch (NumberFormatException e) {
                throw new ResponseException("Expected: play <game number> <color>");
            }
        }
        throw new ResponseException("Error with joining the game.");
    }

    public String observeGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            assertLoggedIn();
            int gameNum = Integer.parseInt(params[0]);
            try {
                GameData game = gameList.get(gameNum - 1);
                new BoardRenderer(game.game().getBoard(), ChessGame.TeamColor.WHITE).render();
                return String.format("Observing game: %s", params[0]);
            } catch (IndexOutOfBoundsException e) {
                throw new ResponseException("Error: Invalid game number.");
            }
        }
        throw new ResponseException("Error with viewing the game.");
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - login
                    - register
                    - quit
                    """;
        }
        return """
                - create game
                - list games
                - play game
                - observe game
                - logout
                """;
    }

    private void assertLoggedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException("Sign in.");
        }
    }
}
