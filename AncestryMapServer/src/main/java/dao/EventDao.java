package dao;

import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import model.*;
import encodeDecode.*;

/**
 * Performs all operations on a database associated with the Event table.
 */
public class EventDao {
    /**
     * Connection belonging to a given instance.
     */
    private Connection conn;

    /**
     * Object containing information for all possible locations for events.
     */
    private LocationList locations;

    /**
     * Keeps track of number of events added by Dao, used in services.
     */
    private int numEvents;

    /**
     * Constructor which establishes a connection and reads in name data from
     * provided JSON files.
     * @param conn Connection belonging to a given instance.
     */
    public EventDao(Connection conn) {
        this.conn = conn;
        this.numEvents = 0;

        try {
            locations = Decoder.decodeLocations(new File("json/locations.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumEvents() {
        return numEvents;
    }

    /**
     * Removes all data from Event table.
     * @throws DataAccessException in case of database error.
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DROP TABLE IF EXISTS Event;";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE Event (" +
                    "eventID TEXT NOT NULL UNIQUE, " +
                    "username TEXT NOT NULL, " +
                    "personID TEXT NOT NULL, " +
                    "latitude REAL NOT NULL, " +
                    "longitude REAL NOT NULL, " +
                    "country TEXT NOT NULL, " +
                    "city TEXT NOT NULL, " +
                    "eventType TEXT NOT NULL, " +
                    "eventYear INTEGER NOT NULL, " +
                    "PRIMARY KEY(eventID));";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to reload Event table");
        }
    }

    /**
     * Adds a new event to the database.
     * @param newEvent event to add to the database.
     * @throws DataAccessException in case of database error.
     */
    public void insert(Event newEvent) throws DataAccessException {
        String sql = "INSERT INTO Event (eventID, username, personID, latitude, " +
                "longitude, country, city, eventType, eventYear) VALUES(?,?,?,?,?,?,?,?,?);";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newEvent.getEventID());
            stmt.setString(2, newEvent.getAssociatedUsername());
            stmt.setString(3, newEvent.getPersonID());
            stmt.setFloat(4, newEvent.getLatitude());
            stmt.setFloat(5, newEvent.getLongitude());
            stmt.setString(6, newEvent.getCountry());
            stmt.setString(7, newEvent.getCity());
            stmt.setString(8, newEvent.getEventType());
            stmt.setInt(9, newEvent.getYear());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to insert into Event table");
        }
        numEvents++;
    }

    /**
     * Checks to see if event exists in the database.
     * @param eventID String value of eventID to find.
     * @return Event object if found in database, null otherwise.
     * @throws DataAccessException in case of database error.
     */
    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Event WHERE eventID = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("eventID"),
                                  rs.getString("username"),
                                  rs.getString("personID"),
                                  rs.getFloat("latitude"),
                                  rs.getFloat("longitude"),
                                  rs.getString("country"),
                                  rs.getString("city"),
                                  rs.getString("eventType"),
                                  rs.getInt("eventYear"));
                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to find event in Event table");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Returns a list of all event objects associated with a given username.
     * @param username String of username of person whose events we want returned.
     * @return List of events associated with a user.
     * @throws DataAccessException in case of database error.
     */
    public Event[] findAll(String username) throws DataAccessException {
        ArrayList<Event> tempList = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM Event WHERE username = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Event tempEvent = new Event(rs.getString("eventID"),
                                            rs.getString("username"),
                                            rs.getString("personID"),
                                            rs.getFloat("latitude"),
                                            rs.getFloat("longitude"),
                                            rs.getString("country"),
                                            rs.getString("city"),
                                            rs.getString("eventType"),
                                            rs.getInt("eventYear"));
                tempList.add(tempEvent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to find all corresponding events in Event table");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return tempList.toArray(new Event[tempList.size()]);
    }

    /**
     * Removes all events from database that are associated with the given username.
     * @param associatedUsername user whose events we are removing from the database.
     */
    public void removeUserEvents(String associatedUsername) throws DataAccessException {
        String sql = "DELETE FROM Event WHERE username = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to remove all corresponding events in Event table");
        }
    }

    /**
     * Checks the Event table and returns a boolean representing whether a given
     * eventID is contained in the table.
     * @param eventID String value of personID of person to find
     * @return true if eventID is found in database, false otherwise.
     * @throws DataAccessException in case of database error.
     */
    public boolean isEventFound(String eventID) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM Event WHERE eventID = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Helper function used to create birth events for newly generated persons.
     * @param username String with username associated with the new event.
     * @param personID String of personID for newly born person.
     * @param year Year that the person was born.
     * @throws DataAccessException in case of database error.
     */
    public void makeBirthday(String username, String personID, int year) throws DataAccessException {
        Location location = locations.getLocations()[new Random().nextInt(977)];
        Event tempEvent = new Event(UUID.randomUUID().toString(), username, personID,
                                location.getLatitude(), location.getLongitude(),
                                location.getCountry(), location.getCity(), "birth", year);
        insert(tempEvent);
    }

    /**
     * Helper function used to create death events for newly generated persons.
     * @param username String with username associated with the new event.
     * @param personID String of personID for deceased person.
     * @param year Year that the person was born.
     * @throws DataAccessException in case of database error.
     */
    public void makeDeath(String username, String personID, int year) throws DataAccessException {
        Location location = locations.getLocations()[new Random().nextInt(977)];
        Event tempEvent = new Event(UUID.randomUUID().toString(), username, personID,
                location.getLatitude(), location.getLongitude(),
                location.getCountry(), location.getCity(), "death",
                (year+60));
        insert(tempEvent);
    }

    /**
     * Helper function used to create marriage events for newly generated persons.
     * @param username String with username associated with the new event.
     * @param fatherID String of personID for groom.
     * @param motherID String of personID for bride.
     * @param year Year that the person was born.
     * @throws DataAccessException in case of database error.
     */
    public void makeMarriage(String username, String fatherID, String motherID, int year) throws DataAccessException {
        Location location = locations.getLocations()[new Random().nextInt(977)];
        Event fatherMarriage = new Event(UUID.randomUUID().toString(), username, fatherID,
                location.getLatitude(), location.getLongitude(),
                location.getCountry(), location.getCity(), "marriage",
                (year-2));
        Event motherMarriage = new Event(UUID.randomUUID().toString(), username, motherID,
                location.getLatitude(), location.getLongitude(),
                location.getCountry(), location.getCity(), "marriage",
                (year-2));
        insert(fatherMarriage);
        insert(motherMarriage);
    }
}
