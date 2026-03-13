package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DatabaseAccessTests {
    DBDataAccess dbAccess;

    @BeforeEach
    void setup() throws DataAccessException {
        dbAccess = new DBDataAccess();
        dbAccess.clear();
    }

    @Test
    void clearSuccess() throws DataAccessException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        dbAccess.createUser(user);
        dbAccess.clear();
        Assertions.assertNull(dbAccess.getUser(user.username()));
    }

    @Test
    void createUserSuccess() throws DataAccessException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        dbAccess.createUser(user);
        UserData userName = dbAccess.getUser(user.username());
        Assertions.assertNotNull(userName);
        Assertions.assertEquals(user.username() ,userName.username());
    }

    @Test
    void createUserFail() throws DataAccessException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        dbAccess.createUser(user);
        Assertions.assertThrows(DataAccessException.class, () -> {
            dbAccess.createUser(user);
        });
    }

    @Test
    void getUserSuccess() throws DataAccessException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        dbAccess.createUser(user);
        UserData userName = dbAccess.getUser(user.username());
        Assertions.assertNotNull(userName);
        Assertions.assertEquals(user.username() ,userName.username());
    }

    @Test
    void getUserFail() throws DataAccessException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        Assertions.assertNull(dbAccess.getUser(user.username()));
    }

    @Test
    void createAuthSuccess() throws DataAccessException {
        UserData user = new UserData("amy", "hello", "email@gmail.com");
        dbAccess.createUser(user);
        AuthData token = dbAccess.createAuth(user);
    }

    @Test
    void createAuthFail() throws DataAccessException {

    }

    @Test
    void getAuthSuccess() throws DataAccessException {

    }

    @Test
    void getAuthFail() throws DataAccessException {

    }

    @Test
    void deleteAuthSuccess() throws DataAccessException {

    }

    @Test
    void deleteAuthFail() throws DataAccessException {

    }

    @Test
    void createGameSuccess() throws DataAccessException {

    }

    @Test
    void createGameFail() throws DataAccessException {

    }

    @Test
    void getGameSuccess() throws DataAccessException {

    }

    @Test
    void getGameFail() throws DataAccessException {

    }

    @Test
    void listGamesSuccess() throws DataAccessException {

    }

    @Test
    void listGamesFail() throws DataAccessException {

    }

    @Test
    void updateGameSuccess() throws DataAccessException {

    }

    @Test
    void updateGameFail() throws DataAccessException {

    }

}
