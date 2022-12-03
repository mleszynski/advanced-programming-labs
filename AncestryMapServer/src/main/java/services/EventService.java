package services;

import dao.*;
import model.*;
import result.EventResult;

/**
 * Handles event request by passing it off to the appropriate Daos.
 */
public class EventService {
    private Database db;

    public EventService() {
        this.db = new Database();
    }

    /**
     * Returns the single Event object with the specified ID (if the event
     * is associated with the current user). The current user is determined
     * by the provided authtoken.
     * @param eventID ID of desired Event object.
     * @param authtoken Token used to identify current user.
     * @return EventResult object with information or error message.
     */
    public EventResult oneEvent(String eventID, String authtoken) {
        EventResult result = new EventResult();

        try {
            db.openConnection();

            if (db.getAuthTokenDao().isTokenFound(authtoken)) {
                String username = db.getAuthTokenDao().find(authtoken).getUsername();

                if (db.getEventDao().isEventFound(eventID)) {
                    Event tempEvent = db.getEventDao().find(eventID);
                    String eventUsername = tempEvent.getAssociatedUsername();

                    if (username.equals(eventUsername)) {
                        result.setAssociatedUsername(tempEvent.getAssociatedUsername());
                        result.setEventID(tempEvent.getEventID());
                        result.setPersonID(tempEvent.getPersonID());
                        result.setLatitude(tempEvent.getLatitude());
                        result.setLongitude(tempEvent.getLongitude());
                        result.setCountry(tempEvent.getCountry());
                        result.setCity(tempEvent.getCity());
                        result.setEventType(tempEvent.getEventType());
                        result.setYear(tempEvent.getYear());
                        result.setSuccess(true);
                        db.closeConnection(true);
                    } else {
                        result.setMessage("Error: This user does not have access to requested event");
                        result.setSuccess(false);
                        db.closeConnection(false);
                    }
                } else {
                    result.setMessage("Error: No match for eventID found");
                    result.setSuccess(false);
                    db.closeConnection(false);
                }
            } else {
                result.setMessage("Error: No such authtoken found");
                result.setSuccess(false);
                db.closeConnection(false);
            }
        } catch (DataAccessException e) {
            result.setMessage("Error: Data access error in EventService");
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
