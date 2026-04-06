package server;

import org.eclipse.jetty.websocket.api.Session;

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
        games.remove(gameID);
    }

    public void broadcast(int gameID, String message, Session excludeSession) {

    }
}
