package services;

import dao.*;
import result.ClearResult;

/**
 * Handles clear request by passing it off to the appropriate Daos.
 * Results in a cleared database. Used mostly as a testing/debugging tool.
 */
public class ClearService {
    private Database db;

    public ClearService() {
        this.db = new Database();
    }

    /**
     * Deletes ALL data from the database, including user, authtoken, person,
     * and event data.
     * @return ClearResult object with information or error message.
     */
    public ClearResult clear() {
        ClearResult result = new ClearResult();

        try {
            db.openConnection();
            db.clearDatabase();
            result.setMessage("Clear succeeded.");
            result.setSuccess(true);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            result.setMessage("Error: Data access error in ClearService");
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
