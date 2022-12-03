package services;

import dao.*;
import model.*;
import result.FillResult;

/**
 * Handles fill request by passing it off to the appropriate Daos.
 */
public class FillService {
    private Database db;

    public FillService() {
        this.db = new Database();
    }

    /**
     * Populates the server's database with generated data for the specified username.
     * The required "username" parameter must be a user already registered with the
     * server. If there is any data in the database already associated with the
     * given username, it is deleted.
     * @param username Username of user whose family tree is populated.
     * @param generations Number of generations to populate for the user. Default is 4.
     * @return FillResult object with information or error message.
     */
    public FillResult fill(String username, int generations) {
        FillResult result = new FillResult();

        try {
            db.openConnection();

            if (db.getUserDao().isUserFound(username) && generations >= 0) {
                User curUser = db.getUserDao().find(username);
                db.getPersonDao().removeUserPersons(username);
                db.getEventDao().removeUserEvents(username);

                if (generations == 101) {
                    db.getPersonDao().makeRoot(db.getEventDao(), curUser, curUser.getPersonID(), 4);
                } else {
                    db.getPersonDao().makeRoot(db.getEventDao(), curUser, curUser.getPersonID(), generations);
                }
                int numEvents = db.getEventDao().getNumEvents();
                int numPeople = db.getPersonDao().getNumPeople();
                result.setMessage("Successfully added " + numPeople + " persons and " +
                                  numEvents + " events to the database.");
                result.setSuccess(true);
                db.closeConnection(true);
            } else {
                result.setMessage("Error: Incorrect User or Generations parameter");
                result.setSuccess(false);
                db.closeConnection(false);
            }
        } catch (DataAccessException e) {
            result.setMessage("Error: Data access error in FillService");
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
