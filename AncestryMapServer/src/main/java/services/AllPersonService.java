package services;

import dao.*;
import result.AllPersonResult;

/**
 * Handles all person request by passing it off to the appropriate Daos.
 */
public class AllPersonService {
    private Database db;

    public AllPersonService() {
        this.db = new Database();
    }

    /**
     * Returns ALL family members of the current user. The current user is
     * determined by the provided authtoken.
     * @param authtoken Token used to identify current user.
     * @return AllPersonResult object with information or error message.
     */
    public AllPersonResult allPersons(String authtoken) {
        AllPersonResult result = new AllPersonResult();

        try {
            db.openConnection();

            if (db.getAuthTokenDao().isTokenFound(authtoken)) {
                String username = db.getAuthTokenDao().find(authtoken).getUsername();
                result.setData(db.getPersonDao().findAll(username));
                result.setSuccess(true);
                db.closeConnection(true);
            } else {
                result.setMessage("Error: No such authtoken found");
                result.setSuccess(false);
                db.closeConnection(false);
            }
        } catch (DataAccessException e) {
            result.setMessage("Error: Data access error in AllPersonService");
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
