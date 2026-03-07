package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;

import java.util.Map;

public class HandlerUtils {
    public static void handleError(Context response, DataAccessException e) {
        if (e.getMessage().contains("bad request")) {
            response.status(400);
        } else if (e.getMessage().contains("unauthorized")) {
            response.status(401);
        } else if (e.getMessage().contains("already taken")) {
            response.status(403);
        } else {
            response.status(500);
        }
        response.result(new Gson().toJson(Map.of("message", e.getMessage())));
    }
}
