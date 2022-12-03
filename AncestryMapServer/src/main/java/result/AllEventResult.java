package result;

import model.Event;

/**
 * Result of an event query meant to return a list of all events related to
 * a user with a specified authtoken,
 * containing given fields, success flags, and error messages when applicable.
 */
public class AllEventResult extends ResultManager {
    /**
     * List of all event objects representing events associated with the current user.
     */
    private Event[] data;

    /**
     * Constructor used when generating result for a successful transaction.
     * @param data List of all event objects representing events associated with the current user.
     */
    public AllEventResult(Event[] data) {
        this.data = data;
        this.setMessage(null);
        this.setSuccess(true);
    }

    /**
     * Constructor used when generating result for a failed transaction.
     * @param message Message describing an error used in error handling.
     */
    public AllEventResult(String message) {
        this.data = null;
        this.setMessage(message);
        this.setSuccess(false);
    }

    public AllEventResult() {}

    public Event[] getData() { return data; }

    public void setData(Event[] data) { this.data = data; }
}
