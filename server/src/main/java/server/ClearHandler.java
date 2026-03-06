package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.ClearService;
import io.javalin.http.Context;

import java.util.Map;

public class ClearHandler {
    private ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public void clear(Context response) {
        try {
            clearService.clear();
            response.status(200);
            response.result("{}");
        } catch (DataAccessException e) {
            response.status(500);
            response.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }
}
