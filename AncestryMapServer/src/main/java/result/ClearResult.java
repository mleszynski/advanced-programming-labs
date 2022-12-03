package result;

/**
 * Result of a clear query, containing given fields, success flags, and
 * error messages when applicable.
 */
public class ClearResult extends ResultManager {
    /**
     * Constructor used when generating a transaction result.
     * @param message Message describing operation success or error details.
     * @param success Boolean representing a successful result if true, unsuccessful if otherwise.
     */
    public ClearResult(String message, boolean success) {
        this.setMessage(message);
        this.setSuccess(success);
    }

    public ClearResult() {}
}
