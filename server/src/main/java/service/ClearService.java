package service;
import dataaccess.DataAccess;

public class ClearService {
    private DataAccess dataAccess;

    public ClearService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear() {
        dataAccess.clear();
    }
}
