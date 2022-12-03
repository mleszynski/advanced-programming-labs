package result;

/**
 * Result of a registration query, containing given fields, success flags, and
 * error messages when applicable.
 */
public class RegisterResult extends ResultManager {
    /**
     * Unique authtoken.
     */
    private String authtoken;

    /**
     * Unique username for user.
     */
    private String username;

    /**
     * Unique identifier for this person.
     */
    private String personID;

    /**
     * Constructor used when generating result for a successful transaction.
     * @param authtoken Unique authtoken.
     * @param username Unique username for user.
     * @param personID Unique identifier for this person.
     */
    public RegisterResult(String authtoken, String username, String personID) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        this.setMessage(null);
        this.setSuccess(true);
    }

    /**
     * Constructor used when generating result for a failed transaction.
     * @param message Message describing an error used in error handling.
     */
    public RegisterResult(String message) {
        this.authtoken = null;
        this.username = null;
        this.personID = null;
        this.setMessage(message);
        this.setSuccess(false);
    }

    public RegisterResult() {}

    public String getAuthtoken() { return authtoken; }

    public void setAuthtoken(String authtoken) { this.authtoken = authtoken; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPersonID() { return personID; }

    public void setPersonID(String personID) { this.personID = personID; }
}
