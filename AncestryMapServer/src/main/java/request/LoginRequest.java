package request;

/**
 * Body of a login query, containing necessary fields.
 * These queries are sent from the client to a Dao to be processed
 * by the server.
 */
public class LoginRequest {
    /**
     * Unique username for user.
     */
    private String username;

    /**
     * User's password.
     */
    private String password;

    /**
     * Constructor used when generating a default LoginRequest object.
     * All fields default to null;
     */
    public LoginRequest() {
        this.username = null;
        this.password = null;
    }

    /**
     * Constructor used when generating a LoginRequest object with desired
     * values in fields.
     * @param username Unique username for user.
     * @param password User's password.
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
}
