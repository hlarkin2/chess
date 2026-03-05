package server;

import service.ClearService;
import io.javalin.http.Context;

public class ClearHandler {
    private ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public void clear(Context response) {
        clearService.clear();
        response.status(200);
        response.result("{}");
    }
}
