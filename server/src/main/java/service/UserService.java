package service;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData user) throws DataAccessException {
        UserData username = dataAccess.getUser(user.username());
        if (username != null) {
            throw new DataAccessException("Error: already taken");
        }
        dataAccess.createUser(user);
        String auth = UUID.randomUUID().toString();
        AuthData authData = new AuthData(auth, user.username());
        dataAccess.createAuth(authData);

        return authData;
    }

    public AuthData login(UserData user) throws DataAccessException {
        UserData username = dataAccess.getUser(user.username());

        if (username == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        if (!username.password().equals(user.password())) {
            throw new DataAccessException("Error: unauthorized");
        }

        String auth = UUID.randomUUID().toString();
        AuthData authData = new AuthData(auth, username.username());
        dataAccess.createAuth(authData);
        return authData;
    }

    public void logout(AuthData token) throws DataAccessException {
        if (dataAccess.getAuth(token.authToken()) == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        dataAccess.deleteAuth(token);
    }
}
