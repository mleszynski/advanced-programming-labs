package result;

import model.Event;

/**
 * Result of an event query meant to return a single event object with a specified ID,
 * containing given fields, success flags, and error messages when applicable.
 */
public class EventResult extends ResultManager {
    /**
     * Username of user to which this event belongs.
     */
    private String associatedUsername;

    /**
     * Unique identifier for this event.
     */
    private String eventID;

    /**
     * ID of person to which this event belongs.
     */
    private String personID;

    /**
     * Latitude of event's location.
     */
    private float latitude;

    /**
     * Longitude of event's location.
     */
    private float longitude;

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
    private int year;

    /**
     * Constructor used when generating result for a successful transaction.
     * @param event Event object that matches the specified ID.
     */
    public EventResult(Event event) {
        this.associatedUsername = event.getAssociatedUsername();
        this.eventID = event.getEventID();
        this.personID = event.getEventID();
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.country = event.getCountry();
        this.city = event.getCity();
        this.eventType = event.getEventType();
        this.year = event.getYear();
        this.setMessage(null);
        this.setSuccess(true);
    }

    /**
     * Constructor used when generating result for a failed transaction.
     * @param message Message describing an error used in error handling.
     */
    public EventResult(String message) {
        this.associatedUsername = null;
        this.eventID = null;
        this.personID = null;
        this.latitude = 0.0f;
        this.longitude = 0.0f;
        this.country = null;
        this.city = null;
        this.eventType = null;
        this.year = 0;
        this.setMessage(message);
        this.setSuccess(false);
    }

    public EventResult() {}

    public String getAssociatedUsername() { return associatedUsername; }

    public void setAssociatedUsername(String associatedUsername) { this.associatedUsername = associatedUsername; }

    public String getEventID() { return eventID; }

    public void setEventID(String eventID) { this.eventID = eventID; }

    public String getPersonID() { return personID; }

    public void setPersonID(String personID) { this.personID = personID; }

    public float getLatitude() { return latitude; }

    public void setLatitude(float latitude) { this.latitude = latitude; }

    public float getLongitude() { return longitude; }

    public void setLongitude(float longitude) { this.longitude = longitude; }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getEventType() { return eventType; }

    public void setEventType(String eventType) { this.eventType = eventType; }

    public int getYear() { return year; }

    public void setYear(int year) { this.year = year; }
}
