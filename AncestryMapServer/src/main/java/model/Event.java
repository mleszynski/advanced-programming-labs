package model;

import java.util.Objects;
import java.util.UUID;

/**
 * Contains information regarding an event that has occurred in a person's life.
 */
public class Event {
    /**
     * Unique identifier for this event.
     */
    private String eventID;

    /**
     * Username of user to which this event belongs.
     */
    private String associatedUsername;

    /**
     * ID of person to which this event belongs.
     */
    private String personID;

    /**
     * Latitude of event's location.
     */
    private Float latitude;

    /**
     * Longitude of event's location.
     */
    private Float longitude;

    /**
     * Country in which event occurred.
     */
    private String country;

    /**
     * City in which event occurred.
     */
    private String city;

    /**
     * Type of event.
     */
    private String eventType;

    /**
     * Year in which event occurred.
     */
    private Integer year;

    /**
     * Creates an event object with all fields set to default values (null or 0).
     * We use UUID.randomUUID() to ensure eventID values are unique.
     */
    public Event() {
        this.eventID = UUID.randomUUID().toString();
        this.associatedUsername = null;
        this.personID = null;
        this.latitude = .0f;
        this.longitude = .0f;
        this.country = null;
        this.city = null;
        this.eventType = null;
        this.year = 0;
    }

    /**
     * Creates an event object with no null fields.
     * @param eventID Unique identifier for this event.
     * @param username Username of user to which this event belongs.
     * @param personID ID of person to which this event belongs.
     * @param latitude Latitude of event's location.
     * @param longitude Longitude of event's location.
     * @param country Country in which event occurred.
     * @param city City in which event occurred.
     * @param eventType Type of event.
     * @param year Year in which event occurred.
     */
    public Event(String eventID, String username, String personID, Float latitude, Float longitude,
                 String country, String city, String eventType, Integer year) {
        this.eventID = eventID;
        this.associatedUsername = username;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() { return eventID; }

    public void setEventID(String eventID) { this.eventID = eventID; }

    public String getAssociatedUsername() { return associatedUsername; }

    public void setAssociatedUsername(String associatedUsername) { this.associatedUsername = associatedUsername; }

    public String getPersonID() { return personID; }

    public void setPersonID(String personID) { this.personID = personID; }

    public Float getLatitude() { return latitude; }

    public void setLatitude(Float latitude) { this.latitude = latitude; }

    public Float getLongitude() { return longitude; }

    public void setLongitude(Float longitude) { this.longitude = longitude; }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getEventType() { return eventType; }

    public void setEventType(String eventType) { this.eventType = eventType; }

    public Integer getYear() { return year; }

    public void setYear(Integer year) { this.year = year; }

    /**
     * Overrides Object equals function to test two events for equality.
     * @param o Object used for comparison against this object.
     * @return true if objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventID, event.eventID) && Objects.equals(associatedUsername, event.associatedUsername) &&
                Objects.equals(personID, event.personID) && Objects.equals(latitude, event.latitude) &&
                Objects.equals(longitude, event.longitude) && Objects.equals(country, event.country) &&
                Objects.equals(city, event.city) && Objects.equals(eventType, event.eventType) &&
                Objects.equals(year, event.year);
    }

    /**
     * Auto-generated hash-code function.
     * @return hash value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(eventID, associatedUsername, personID, latitude, longitude, country, city, eventType, year);
    }

}
