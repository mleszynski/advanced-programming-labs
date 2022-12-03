package result;

import model.Person;

/**
 * Result of a person query meant to return a single person object with a specified ID,
 * containing given fields, success flags, and error messages when applicable.
 */
public class PersonResult extends ResultManager {
    /**
     * Username of user to which this person belongs.
     */
    private String associatedUsername;

    /**
     * Unique identifier for this person.
     */
    private String personID;

    /**
     * Person's first name.
     */
    private String firstName;

    /**
     * Person's last name.
     */
    private String lastName;

    /**
     * Person's gender (acceptable values are "f" or "m").
     */
    private String gender;

    /**
     * Person ID of person's father (may be null).
     */
    private String fatherID;

    /**
     * Person ID of person's mother (may be null).
     */
    private String motherID;

    /**
     * Person ID of person's spouse (may be null).
     */
    private String spouseID;

    /**
     * Constructor used when generating result for a successful transaction.
     * @param person Person object that matches the specified ID.
     */
    public PersonResult(Person person) {
        this.associatedUsername = person.getAssociatedUsername();
        this.personID = person.getPersonID();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.gender = person.getGender();
        this.fatherID = person.getFatherID();
        this.motherID = person.getMotherID();
        this.spouseID = person.getSpouseID();
        this.setMessage(null);
        this.setSuccess(true);
    }

    /**
     * Constructor used when generating result for a failed transaction.
     * @param message Message describing an error used in error handling.
     */
    public PersonResult(String message) {
        this.associatedUsername = null;
        this.personID = null;
        this.firstName = null;
        this.lastName = null;
        this.gender = null;
        this.fatherID = null;
        this.motherID = null;
        this.spouseID = null;
        this.setMessage(message);
        this.setSuccess(false);
    }

    public PersonResult() {}

    public String getAssociatedUsername() { return associatedUsername; }

    public void setAssociatedUsername(String associatedUsername) { this.associatedUsername = associatedUsername; }

    public String getPersonID() { return personID; }

    public void setPersonID(String personID) { this.personID = personID; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getFatherID() { return fatherID; }

    public void setFatherID(String fatherID) { this.fatherID = fatherID; }

    public String getMotherID() { return motherID; }

    public void setMotherID(String motherID) { this.motherID = motherID; }

    public String getSpouseID() { return spouseID; }

    public void setSpouseID(String spouseID) { this.spouseID = spouseID; }
}
