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
                case MAKE_MOVE -> handleMakeMove(session, command);
                case LEAVE -> handleLeave(session, command);
                case RESIGN -> handleResign(session, command);
            }
        } catch (IOException msg) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: unable to read command")));
        }
    }

    private void handleConnect(Session session, UserGameCommand command) throws IOException {
        try {
            AuthData auth = dataAccess.getAuth(command.getAuthToken());
            GameData game = dataAccess.getGame(command.getGameID());

            if (auth == null || game == null) {
                session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: unable find session")));
                return;
            }

            connectionManager.add(command.getGameID(), session);
            session.getRemote().sendString(new Gson().toJson(new LoadGameMessage(game.game())));

            String user;
            if (auth.username().equals(game.whiteUsername())) {
                user = game.whiteUsername();
            } else if (auth.username().equals(game.blackUsername())) {
                user = game.blackUsername();
            } else {
                user = "observer";
            }

            NotificationMessage message = new NotificationMessage("Connecting " + user + " to the game");
            connectionManager.broadcast(game.gameID(), new Gson().toJson(message), session);

        } catch (DataAccessException message) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: unable to connect")));
        }
    }

    private void handleMakeMove(Session session, UserGameCommand command) throws IOException {
        try {
            AuthData auth = dataAccess.getAuth(command.getAuthToken());
            GameData game = dataAccess.getGame(command.getGameID());

            if (auth == null || game == null) {
                session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: unable find session")));
                return;
            }

            String user;
            if (auth.username().equals(game.whiteUsername())) {
                user = game.whiteUsername();
            } else if (auth.username().equals(game.blackUsername())) {
                user = game.blackUsername();
            } else {
                user = "observer";
            }

            if (game.game().isGameOver()) {
                session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: unable to join ended game")));
                return;
            }

            ChessMove move = ((MakeMoveCommand) command).getMove();

            try {
                game.game().makeMove(move);
            } catch (InvalidMoveException message) {
                session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: invalid move")));
                return;
            }

            dataAccess.updateGame(game);

            NotificationMessage message = new NotificationMessage(user + " made the move: " + move);
            connectionManager.broadcast(game.gameID(), new Gson().toJson(message), session);
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

        } catch (DataAccessException message) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: unable to connect")));
        }
    }

    private void handleLeave(Session session, UserGameCommand command) throws IOException {
        try {
            AuthData auth = dataAccess.getAuth(command.getAuthToken());
            GameData game = dataAccess.getGame(command.getGameID());

            if (auth == null || game == null) {
                session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: unable find session")));
                return;
            }

            String user;
            if (auth.username().equals(game.whiteUsername())) {
                user = game.whiteUsername();
            } else if (auth.username().equals(game.blackUsername())) {
                user = game.blackUsername();
            } else {
                user = "observer";
            }

            GameData updatedGame = game;

            if (!user.equals("observer")) {
                if (auth.username().equals(game.whiteUsername())) {
                    updatedGame = new GameData(game.gameID(), null, game.blackUsername(), game.gameName(), game.game());
                } else if (auth.username().equals(game.blackUsername())) {
                    updatedGame = new GameData(game.gameID(), game.whiteUsername(), null, game.gameName(), game.game());
                }
                dataAccess.updateGame(updatedGame);
            }

            connectionManager.remove(game.gameID(), session);
            connectionManager.broadcast(game.gameID(), new Gson().toJson(new NotificationMessage(auth.username() + " left the game")), session);

        } catch (DataAccessException message) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: error with leaving game")));
        }
    }

    private void handleResign(Session session, UserGameCommand command) throws IOException {
        try {
            AuthData auth = dataAccess.getAuth(command.getAuthToken());
            GameData game = dataAccess.getGame(command.getGameID());

            if (auth == null || game == null) {
                session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: unable find session")));
                return;
            }

            String user;
            if (auth.username().equals(game.whiteUsername())) {
                user = game.whiteUsername();
            } else if (auth.username().equals(game.blackUsername())) {
                user = game.blackUsername();
            } else {
                user = "observer";
            }

            if (game.game().isGameOver()) {
                session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: unable to resign ended game")));
                return;
            }

            if (!user.equals("observer")) {
                game.game().setGameEnded();
                dataAccess.updateGame(game);
            } else {
                session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: observers are unable to resign game")));
                return;
            }

            connectionManager.broadcast(game.gameID(), new Gson().toJson(new NotificationMessage(auth.username() + " has resigned the game")), null);

        } catch (DataAccessException message) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: error with resigning game")));
        }
    }
}
