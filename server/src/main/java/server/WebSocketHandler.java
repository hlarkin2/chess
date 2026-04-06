package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import io.javalin.websocket.WsMessageContext;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
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

    public void onMessage(WsMessageContext context) {
        String message = context.message();
        Session session = context.session;

        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        switch (command.getCommandType()) {
            case CONNECT -> handleConnect(session, command);
            case MAKE_MOVE -> handleMakeMove(session, command);
            case LEAVE -> handleLeave(session, command);
            case RESIGN -> handleResign(session, command);
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

            NotificationMessage message = new Gson().toJson("Connecting to game");
            connectionManager.broadcast(game.gameID(), message, session);

        } catch (DataAccessException message) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Error: unable to connect")));
        }
    }

    private void handleMakeMove(Session session, UserGameCommand command) {

    }

    private void handleLeave(Session session, UserGameCommand command) {

    }

    private void handleResign(Session session, UserGameCommand command) {

    }
}
