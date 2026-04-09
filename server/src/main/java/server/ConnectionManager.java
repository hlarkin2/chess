package server;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConnectionManager {
    Map<Integer, Set<Session>> games = new HashMap<>();

    public void add(int gameID, Session session) {
        games.computeIfAbsent(gameID, newList -> new HashSet<>()).add(session);
    }

    public void remove(int gameID, Session session) {
        games.get(gameID).remove(session);
    }

    public void broadcast(int gameID, String message, Session excludeSession) throws IOException {
        Set<Session> sessions = games.get(gameID);
        if (sessions == null) {
            return;
        }

        for (Session session : sessions) {
            if (session.equals(excludeSession)) {
                continue;
            }
            if (!session.isOpen()) {
                continue;
            }
            session.getRemote().sendString(message);
        }
    }
}
