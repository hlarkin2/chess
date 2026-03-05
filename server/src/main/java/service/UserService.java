package service;
import dataaccess.DataAccess;

public class UserService {
    private DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public createUser(dataAccess) {
        dataAccess.createUser();
    }

    public getUser(dataAccess) {
        dataAccess.getUser();
    }
}
