package services;

import dao.*;
import model.*;
import result.AllEventResult;

/**
 * Handles all-event request by passing it off to the appropriate Daos.
 */
public class AllEventService {
    private Database db;

    public AllEventService() {
        this.db = new Database();
    }

    /**
     * Returns ALL events for ALL family members of the current user. The current
     * user is determined from the provided authtoken.
     * @param authtoken Token used to identify current user.
     * @return AllEventResult object with information or error message.
     */
    public AllEventResult allEvents(String authtoken) {
        AllEventResult result = new AllEventResult();

        try {
            db.openConnection();

            if (db.getAuthTokenDao().isTokenFound(authtoken)) {
                String username = db.getAuthTokenDao().find(authtoken).getUsername();
                result.setData(db.getEventDao().findAll(username));
                result.setSuccess(true);
                db.closeConnection(true);
            } else {
                result.setMessage("Error: No such authtoken found");
                result.setSuccess(false);
                db.closeConnection(false);
            }
        } catch (DataAccessException e) {
            result.setMessage("Error: Data access error in AllEventService");
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
