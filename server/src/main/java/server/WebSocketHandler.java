package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;

public class WebSocketHandler {
    private ConnectionManager connectionManager;

    public WebSocketHandler(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void onMessage(Session session, String message) {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        switch (command.getCommandType()) {
            case CONNECT -> handleConnect(session, command);
            case MAKE_MOVE -> handleMakeMove(session, command);
            case LEAVE -> handleLeave(session, command);
            case RESIGN -> handleResign(session, command);
        }
    }

    private void handleConnect(Session session, UserGameCommand command) {

    }

    private void handleMakeMove(Session session, UserGameCommand command) {

    }

    private void handleLeave(Session session, UserGameCommand command) {

    }

    private void handleResign(Session session, UserGameCommand command) {

    }
}
