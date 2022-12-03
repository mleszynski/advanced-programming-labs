package services;

import java.util.*;
import dao.*;
import model.*;
import request.RegisterRequest;
import result.RegisterResult;

/**
 * Handles registration request by passing it off to the appropriate Daos.
 */
public class RegisterService {
    private Database db;

    public RegisterService() {
        this.db = new Database();
    }

    /**
     * Creates a new user account (user row in the database).
     * @param r request sent to Dao in order to process registration.
     * @return RegisterResult object with information or error message.
     */
    public RegisterResult register(RegisterRequest r) {
        RegisterResult result = new RegisterResult();

        try {
            db.openConnection();
            boolean goodUname = !r.getUsername().isEmpty();
            boolean goodPass = !r.getPassword().isEmpty();
            boolean goodEmail = !r.getEmail().isEmpty();
            boolean goodFName = !r.getFirstName().isEmpty();
            boolean goodLName = !r.getLastName().isEmpty();
            boolean goodGendr = (r.getGender().equals("m") || r.getGender().equals("f"));

            if (goodUname && goodPass && goodEmail && goodFName && goodLName && goodGendr) {

                if (!db.getUserDao().isUserFound(r.getUsername())) {
                    String tempPersonID = UUID.randomUUID().toString();
                    User tempUser = new User(r.getUsername(),
                                             r.getPassword(),
                                             r.getEmail(),
                                             r.getFirstName(),
                                             r.getLastName(),
                                             r.getGender(),
                                             tempPersonID);
                    db.getUserDao().insert(tempUser);
                    db.getPersonDao().makeRoot(db.getEventDao(), tempUser, tempPersonID, 4);
                    String tempToken = UUID.randomUUID().toString();
                    Authtoken authtoken = new Authtoken(tempToken, tempUser.getUsername());
                    db.getAuthTokenDao().insert(authtoken);
                    result.setAuthtoken(tempToken);
                    result.setUsername(tempUser.getUsername());
                    result.setPersonID(tempUser.getPersonID());
                    result.setSuccess(true);
                    db.closeConnection(true);
                } else {
                    result.setMessage("Error: Username already exists");
                    result.setSuccess(false);
                    db.closeConnection(false);
                }
            } else {
                result.setMessage("Error: Registration values are missing or invalid.");
                result.setSuccess(false);
                db.closeConnection(false);
            }
        } catch (DataAccessException e) {
            result.setMessage("Error: Data access error in RegisterService");
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
