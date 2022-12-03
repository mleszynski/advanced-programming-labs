package model;

/**
 * Contains information regarding the location of an event (used for Event handling).
 */
public class Location {
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

    public Location() {}

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
