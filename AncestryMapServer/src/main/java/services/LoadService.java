package services;

import dao.*;
import request.LoadRequest;
import result.LoadResult;

/**
 * Handles load request by passing it off to the appropriate Daos.
 */
public class LoadService {
    private Database db;

    public LoadService() {
        this.db = new Database();
    }

    /**
     * Clears all data from the database (just like the /clear API) and loads
     * the user, person, and event data from the request body into the database.
     * @param r request sent to Dao in order to process load.
     * @return LoadResult object with information or error message.
     */
    public LoadResult load(LoadRequest r) {
        LoadResult result = new LoadResult();

        try {
            db.openConnection();
            db.clearDatabase();
            for (int i = 0; i < r.getUsers().length; i++) {
                db.getUserDao().insert(r.getUsers()[i]);
            }
            for (int i = 0; i < r.getPersons().length; i++) {
                db.getPersonDao().insert(r.getPersons()[i]);
            }
            for (int i = 0; i < r.getEvents().length; i++) {
                db.getEventDao().insert(r.getEvents()[i]);
            }
            result.setMessage("Successfully added " + db.getUserDao().getNumUsers() +
                              " users, " + db.getPersonDao().getNumPeople() +
                              " persons, and " + db.getEventDao().getNumEvents() +
                              " events to the database.");
            result.setSuccess(true);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            result.setMessage("Error: Data access error in LoadService");
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
