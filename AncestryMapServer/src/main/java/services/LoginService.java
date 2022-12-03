package services;

import java.util.*;
import dao.*;
import model.*;
import request.LoginRequest;
import result.LoginResult;

/**
 * Handles login request by passing it off to the appropriate Daos.
 */
public class LoginService {
    private Database db;

    public LoginService() {
        this.db = new Database();
    }

    /**
     * Logs the user in.
     * @param r request sent to Dao in order to process log in.
     * @return LoginResult object with information or error message.
     */
    public LoginResult login(LoginRequest r) {
        LoginResult result = new LoginResult();

        try {
            db.openConnection();
            String username = r.getUsername();
            String password = r.getPassword();

            if (db.getUserDao().isUserFound(username) &&
                password.equals(db.getUserDao().find(username).getPassword())) {
                String newToken = UUID.randomUUID().toString();
                String prevPersonID = db.getUserDao().find(username).getPersonID();

                if (db.getAuthTokenDao().isUsernameFound(username)) {
                    db.getAuthTokenDao().updateToken(newToken, username);
                } else {
                    Authtoken tempToken = new Authtoken(newToken, username);
                    db.getAuthTokenDao().insert(tempToken);
                }
                result.setAuthtoken(newToken);
                result.setUsername(username);
                result.setPersonID(db.getUserDao().find(username).getPersonID());
                result.setSuccess(true);
                db.closeConnection(true);
            } else {
                result.setMessage("Error: No such username or password");
                result.setSuccess(false);
                db.closeConnection(false);
            }
        } catch (DataAccessException e) {
            result.setMessage("Error: Data access error in LoginService");
            result.setSuccess(false);

            try {
                db.closeConnection(false);
            } catch (DataAccessException d) {
                d.printStackTrace();
            }
        }
        return result;
    }
}
