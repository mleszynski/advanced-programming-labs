package result;

/**
 * Handles common functionality for all result messages.
 */
public class ResultManager {
    /**
     * Message describing an error used in error handling.
     */
    private String message;

    /**
     * Boolean representing a successful result if true, unsuccessful if otherwise.
     */
    private boolean success;

    /**
     * Default constructor.
     */
    public ResultManager() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
