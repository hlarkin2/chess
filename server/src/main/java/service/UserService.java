package service;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.UserData;

public class UserService {
    private DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void createUser(UserData user) throws DataAccessException {
        dataAccess.createUser(user);
    }

    public UserData getUser(UserData user) throws DataAccessException {
        return dataAccess.getUser(user.username());
    }
}
