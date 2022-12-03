package model;

import java.util.Objects;

/**
 * Contains information regarding a unique user.
 */
public class User {
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
     * Unique Person ID assigned to this user's generated Person.
     */
    private String personID;

    /**
     * Creates a user object with all fields set to default values (null).
     */
    public User() {
        this.username = null;
        this.password = null;
        this.email = null;
        this.firstName = null;
        this.lastName = null;
        this.gender = null;
        this.personID = null;
    }

    /**
     * Creates a user object with no null fields.
     * @param username Unique username for user.
     * @param password User's password.
     * @param email User's email address.
     * @param firstName User's first name.
     * @param lastName User's last name.
     * @param gender User's gender (acceptable values are "f" or "m").
     * @param personID Unique Person ID assigned to this user's generated Person.
     */
    public User(String username, String password, String email, String firstName,
                String lastName, String gender, String personID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
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

    public String getPersonID() { return personID; }

    public void setPersonID(String personID) { this.personID = personID; }

    /**
     * Overrides Object equals function to test two users for equality.
     * @param o Object used for comparison against this object.
     * @return true if objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(gender, user.gender) && Objects.equals(personID, user.personID);
    }

    /**
     * Auto-generated hash-code function.
     * @return hash value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, password, email, firstName, lastName, gender, personID);
    }
}
