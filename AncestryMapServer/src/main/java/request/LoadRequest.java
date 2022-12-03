package request;

import model.*;

/**
 * Body of a load query, containing necessary fields.
 * These queries are sent from the client to a Dao to be processed
 * by the server.
 */
public class LoadRequest {
    /**
     * List of users to load from the database.
     */
    private User[] users;

    /**
     * List of persons to load from the database.
     */
    private Person[] persons;

    /**
     * List of events to load from the database.
     */
    private Event[] events;

    /**
     * Constructor used when generating a default LoadRequest object.
     * All fields default to null;
     */
    public LoadRequest() {
        this.users = null;
        this.persons = null;
        this.events = null;
    }

    /**
     * Constructor used when generating a LoadRequest object with desired
     * values in fields.
     * @param users List of users to load from the database.
     * @param persons List of persons to load from the database.
     * @param events List of events to load from the database.
     */
    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public User[] getUsers() { return users; }

    public void setUsers(User[] users) { this.users = users; }

    public Person[] getPersons() { return persons; }

    public void setPersons(Person[] persons) { this.persons = persons; }

    public Event[] getEvents() { return events; }
    
    public void setEvents(Event[] events) { this.events = events; }
}
