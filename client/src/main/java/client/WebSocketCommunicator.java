package client;

import jakarta.websocket.*;
import jakarta.websocket.ContainerProvider;
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

    public void send(String message) {

    }

    @OnMessage
    public void onMessage(String message) {

    }
}
