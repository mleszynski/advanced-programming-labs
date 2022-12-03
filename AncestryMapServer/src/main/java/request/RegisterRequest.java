package request;

/**
 * Body of a registration query, containing necessary fields.
 * These queries are sent from the client to a Dao to be processed
 * by the server.
 */
public class RegisterRequest {
    /**
     * Unique username for user.
     */
    private String username;

    /**
     * User's password.
     */
    private String password;

    /**
     * User's email address.
     */
    private String email;

    /**
     * User's first name.
     */
    private String firstName;

    /**
     * User's last name.
     */
    private String lastName;

    /**
     * User's gender (acceptable values are "f" or "m").
     */
    private String gender;

    /**
     * Constructor used when generating a default RegisterRequest object.
     * All fields default to null;
     */
    public RegisterRequest() {
        this.username = null;
        this.password = null;
        this.email = null;
        this.firstName = null;
        this.lastName = null;
        this.gender = null;
    }

    /**
     * Constructor used when generating a RegisterRequest object with desired
     * values in fields.
     * @param username Unique username for user.
     * @param password User's password.
     * @param email User's email address.
     * @param firstName User's first name.
     * @param lastName User's last name.
     * @param gender User's gender (acceptable values are "f" or "m").
     */
    public RegisterRequest(String username, String password, String email,
                           String firstName, String lastName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }
}
