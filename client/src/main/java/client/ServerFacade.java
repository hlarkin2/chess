package client;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import model.GameData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    private final String serverUrl;
    private record ErrorMessage(String message) {}

    public ServerFacade(int port) {
        serverUrl = "http://localhost:" + port;
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    public AuthData register(UserData user) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public AuthData login(UserData user) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public void logout(AuthData auth) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, auth.authToken());
    }

    public GameData listGames(AuthData auth) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, GameData.class, auth.authToken());
    }

    public GameData createGame(GameData game, AuthData auth) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, game.gameName(), GameData.class, auth.authToken());
    }

    public void joinGame(GameData game, AuthData auth) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, game.gameID(), GameData.class, auth.authToken());
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String token) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");
            if (token != null) {
                http.addRequestProperty("authorization", token);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception e) {
            throw new ResponseException(e.getMessage(), e);
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            String requestData = new Gson().toJson(request);
            try (OutputStream requestBody = http.getOutputStream()) {
                requestBody.write(requestData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            InputStream errorStream = http.getErrorStream();
            InputStreamReader reader = new InputStreamReader(errorStream);
            ErrorMessage message = new Gson().fromJson(reader, ErrorMessage.class);
            throw new ResponseException("Error " + status + ": " + message.message());
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream responseBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(responseBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
