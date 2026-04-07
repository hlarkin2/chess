package client;

import com.google.gson.Gson;
import jakarta.websocket.*;
import jakarta.websocket.ContainerProvider;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class WebSocketCommunicator {
    private ServerMessageObserver observer;
    private Session session;

    public WebSocketCommunicator(ServerMessageObserver observer, String serverUrl) throws Exception {
        this.observer = observer;
        URI uri = URI.create(serverUrl + "/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);
    }

    public void send(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    @OnMessage
    public void onMessage(String message) {
        observer.notify(new Gson().fromJson(message, ServerMessage.class));
    }
}
