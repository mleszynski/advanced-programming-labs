package model;

import java.util.Objects;
import java.util.UUID;

/**
 * Contains information regarding a unique person.
 */
public class Person {
    /**
     * Unique identifier for this person.
     */
    private String personID;

    /**
     * Username of user to which this person belongs.
     */
    private String associatedUsername;

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
     * Creates a person object with all fields set to default values (null).
     * We use UUID.randomUUID() to ensure personID values are unique.
     */
    public Person() {
        this.personID = UUID.randomUUID().toString();
        this.associatedUsername = null;
        this.firstName = null;
        this.lastName = null;
        this.gender = null;
        this.fatherID = null;
        this.motherID = null;
        this.spouseID = null;
    }

    /**
     * Creates a person object with no null fields.
     * @param personID Unique identifier for this person.
     * @param associatedUsername Username of user to which this person belongs.
     * @param firstName Person's first name.
     * @param lastName Person's last name.
     * @param gender Person's gender (acceptable values are "f" or "m").
     * @param fatherID Person ID of person's father (may be null).
     * @param motherID Person ID of person's mother (may be null).
     * @param spouseID Person ID of person's spouse (may be null).
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName, String gender,
                  String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getPersonID() { return personID; }

    public void setPersonID(String personID) { this.personID = personID; }

    public String getAssociatedUsername() { return associatedUsername; }

    public void setAssociatedUsername(String associatedUsername) { this.associatedUsername = associatedUsername; }

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

    /**
     * Overrides Object equals function to test two persons for equality.
     * @param o Object used for comparison against this object.
     * @return true if objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(personID, person.personID) && Objects.equals(associatedUsername, person.associatedUsername) && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(gender, person.gender) && Objects.equals(fatherID, person.fatherID) && Objects.equals(motherID, person.motherID) && Objects.equals(spouseID, person.spouseID);
    }

    /**
     * Auto-generated hash-code function.
     * @return hash value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(personID, associatedUsername, firstName, lastName, gender, fatherID, motherID, spouseID);
    }
}
