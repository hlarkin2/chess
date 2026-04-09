package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import io.javalin.websocket.WsMessageContext;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

public class WebSocketHandler {
    private ConnectionManager connectionManager;
    private DataAccess dataAccess;

    public WebSocketHandler(ConnectionManager connectionManager, DataAccess dataAccess) {
        this.connectionManager = connectionManager;
        this.dataAccess = dataAccess;
    }

    public void onMessage(WsMessageContext context) throws IOException {
        String message = context.message();
        Session session = context.session;

        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        try {
            switch (command.getCommandType()) {
                case CONNECT -> handleConnect(session, command);
                case MAKE_MOVE -> handleMakeMove(session, new Gson().fromJson(message, MakeMoveCommand.class));
                case LEAVE -> handleLeave(session, command);
                case RESIGN -> handleResign(session, command);
            }
        } catch (IOException msg) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: unable to read command")));
        }
    }

    private record SessionData(AuthData auth, GameData game) {}

    private SessionData getSessionData(UserGameCommand command) throws DataAccessException {
        AuthData auth = dataAccess.getAuth(command.getAuthToken());
        GameData game = dataAccess.getGame(command.getGameID());
        if (auth == null || game == null) {
            throw new DataAccessException("Error: unable to find session");
        }
        return new SessionData(auth, game);
    }

    private String getUserRole(AuthData auth, GameData game) {
        if (auth.username().equals(game.whiteUsername())) {
            return game.whiteUsername();
        } else if (auth.username().equals(game.blackUsername())) {
            return game.blackUsername();
        } else {
            return "observer";
        }
    }

    private void sendError(Session session, String message) throws IOException {
        session.getRemote().sendString(new Gson().toJson(new ErrorMessage(message)));
    }

    private void handleConnect(Session session, UserGameCommand command) throws IOException {
        try {
            SessionData sessionData = getSessionData(command);
            AuthData auth = sessionData.auth();
            GameData game = sessionData.game();

            connectionManager.add(command.getGameID(), session);
            session.getRemote().sendString(new Gson().toJson(new LoadGameMessage(game.game())));

            NotificationMessage message;
            if (auth.username().equals(game.whiteUsername())) {
                message = new NotificationMessage(auth.username() + " joined the game as WHITE");
            } else if (auth.username().equals(game.blackUsername())) {
                message = new NotificationMessage(auth.username() + " joined the game as BLACK");
            } else {
                message = new NotificationMessage(auth.username() + " joined the game as an observer");
            }

            connectionManager.broadcast(game.gameID(), new Gson().toJson(message), session);

        } catch (DataAccessException e) {
            sendError(session, e.getMessage());
        }
    }

    private void handleMakeMove(Session session, MakeMoveCommand command) throws IOException {
        try {
            SessionData sessionData = getSessionData(command);
            AuthData auth = sessionData.auth();
            GameData game = sessionData.game();
            String user = getUserRole(auth, game);

            if (user.equals("observer")) {
                sendError(session, "Error: observers cannot make moves");
                return;
            }

            if (game.game().isGameOver()) {
                sendError(session, "Error: unable to join ended game");
                return;
            }

            ChessGame.TeamColor playerColor = auth.username().equals(game.whiteUsername())
                    ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

            if (playerColor != game.game().getTeamTurn()) {
                sendError(session, "Error: Not your turn");
                return;
            }

            ChessMove move = command.getMove();
            try {
                game.game().makeMove(move);
            } catch (InvalidMoveException e) {
                sendError(session, "Error: invalid move");
                return;
            }

            dataAccess.updateGame(game);

            connectionManager.broadcast(game.gameID(), new Gson().toJson(new NotificationMessage(user + " made the move: " + move)), session);
            connectionManager.broadcast(game.gameID(), new Gson().toJson(new LoadGameMessage(game.game())), null);

            ChessGame.TeamColor opponent = game.game().getTeamTurn();
            if (game.game().isInCheckmate(opponent)) {
                game.game().setGameEnded();
                dataAccess.updateGame(game);
                connectionManager.broadcast(game.gameID(), new Gson().toJson(new NotificationMessage(opponent + " is in checkmate")), null);
            } else if (game.game().isInStalemate(opponent)) {
                game.game().setGameEnded();
                dataAccess.updateGame(game);
                connectionManager.broadcast(game.gameID(), new Gson().toJson(new NotificationMessage(opponent + " is in stalemate")), null);
            } else if (game.game().isInCheck(opponent)) {
                connectionManager.broadcast(game.gameID(), new Gson().toJson(new NotificationMessage(opponent + " is in check")), null);
            }

        } catch (DataAccessException e) {
            sendError(session, e.getMessage());
        }
    }

    private void handleLeave(Session session, UserGameCommand command) throws IOException {
        try {
            SessionData sessionData = getSessionData(command);
            AuthData auth = sessionData.auth();
            GameData game = sessionData.game();
            String user = getUserRole(auth, game);

            if (!user.equals("observer")) {
                GameData updatedGame;
                if (auth.username().equals(game.whiteUsername())) {
                    updatedGame = new GameData(game.gameID(), null, game.blackUsername(), game.gameName(), game.game());
                } else {
                    updatedGame = new GameData(game.gameID(), game.whiteUsername(), null, game.gameName(), game.game());
                }
                dataAccess.updateGame(updatedGame);
            }

            connectionManager.remove(game.gameID(), session);
            connectionManager.broadcast(game.gameID(), new Gson().toJson(new NotificationMessage(auth.username() + " left the game")), session);

        } catch (DataAccessException e) {
            sendError(session, e.getMessage());
        }
    }

    private void handleResign(Session session, UserGameCommand command) throws IOException {
        try {
            SessionData sessionData = getSessionData(command);
            AuthData auth = sessionData.auth();
            GameData game = sessionData.game();
            String user = getUserRole(auth, game);

            if (game.game().isGameOver()) {
                sendError(session, "Error: unable to resign ended game");
                return;
            }

            if (user.equals("observer")) {
                sendError(session, "Error: observers are unable to resign game");
                return;
            }

            game.game().setGameEnded();
            dataAccess.updateGame(game);
            connectionManager.broadcast(game.gameID(), new Gson().toJson(new NotificationMessage(auth.username() + " has resigned the game")), null);

        } catch (DataAccessException e) {
            sendError(session, e.getMessage());
        }
    }
}