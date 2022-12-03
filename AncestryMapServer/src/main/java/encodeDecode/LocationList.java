package encodeDecode;

import model.Location;

/**
 * Class which contains a list of locations used for person event generation.
 */
public class LocationList {
    /**
     * List of possible location objects containing information for event generation.
     */
    private Location[] data;

    public LocationList() { this.data = new Location[977]; }

    public Location[] getLocations() {
        return data;
    }

    public void setLocations(Location[] locations) {
        this.data = locations;
    }
}
