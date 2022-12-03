package model;

import java.util.Objects;
import java.util.UUID;

/**
 * Contains information regarding a randomly generated authtoken for a user's session.
 */
public class Authtoken {
    /**
     * Unique authtoken.
     */
    private String authtoken;

    /**
     * Username that is associated with the authtoken.
     */
    private String username;

    /**
     * Creates an authtoken object with all fields set to default values (null).
     * We use UUID.randomUUID() to ensure authtoken values are unique.
     */
    public Authtoken() {
        this.authtoken = UUID.randomUUID().toString();
        this.username = null;
    }

    /**
     * Creates an authtoken object with no null fields.
     * @param authtoken Unique authtoken.
     * @param username Username that is associated with the authtoken.
     */
    public Authtoken(String authtoken, String username) {
        this.authtoken = authtoken;
        this.username = username;
    }

    public String getAuthtoken() { return authtoken; }

    public void setAuthtoken(String authtoken) { this.authtoken = authtoken; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    /**
     * Overrides Object equals function to test two authtokens for equality.
     * @param o Object used for comparison against this object.
     * @return true if objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authtoken authtoken1 = (Authtoken) o;
        return Objects.equals(authtoken, authtoken1.authtoken) && Objects.equals(username, authtoken1.username);
    }

    /**
     * Auto-generated hash-code function.
     * @return hash value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(authtoken, username);
    }
}
