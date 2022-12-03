package ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.*;
import services.*;
import result.*;
import request.*;

public class LoadSerTest {
    private RegisterService registerService;
    private LoadService loadService;
    private User user1;
    private User user2;
    private Person person1;
    private Person person2;
    private Event event1;
    private Event event2;

    @BeforeEach
    public void setUp() {
        this.registerService = new RegisterService();
        this.loadService = new LoadService();
        this.user1 = new User("mleszynski", "thisismypass", "temp@gmail.com",
                              "Marc", "Lesser", "m", "myID123");
        this.user2 = new User("leszynski200", "thisismypass", "temp@gmail.com",
                              "Marc", "Lesser", "m", "myID321");
        this.person1 = new Person("myID123", "mleszynski", "Ethan", "Hunt",
                                   "m", "dadID123", "momID123", "spouseID123");
        this.person2 = new Person("myID321", "mleszynski", "Mister", "Hunt",
                                   "m", "dadID123", "momID123", "spouseID123");
        this.event1 = new Event("eventID123", "mleszynski", "myID123", 81.6f,
                               -15.3333f, "Denmark", "Nord", "birth", 1996);
        this.event2 = new Event("eventID321", "mleszynski", "myID123", 81.6f,
                               -15.3333f, "Denmark", "Nord", "mission", 2014);
    }

    @AfterEach
    public void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void loadSingle() {
        // loads a user, a person, and an event.
        User[] userList = new User[1];
        userList[0] = user1;
        Person[] personList = new Person[1];
        personList[0] = person1;
        Event[] eventList = new Event[1];
        eventList[0] = event1;
        LoadRequest loadRequest = new LoadRequest();
        loadRequest.setUsers(userList);
        loadRequest.setPersons(personList);
        loadRequest.setEvents(eventList);
        LoadResult result = new LoadResult();
        result = loadService.load(loadRequest);
        assertTrue(result.isSuccess());
    }

    @Test
    public void loadMultiple() {
        // loads two users, persons, and events to the database.
        User[] userList = new User[2];
        userList[0] = user1;
        userList[1] = user2;
        Person[] personList = new Person[2];
        personList[0] = person1;
        personList[1] = person2;
        Event[] eventList = new Event[2];
        eventList[0] = event1;
        eventList[1] = event2;
        LoadRequest loadRequest = new LoadRequest();
        loadRequest.setUsers(userList);
        loadRequest.setPersons(personList);
        loadRequest.setEvents(eventList);
        LoadResult result = new LoadResult();
        result = loadService.load(loadRequest);
        assertTrue(result.isSuccess());
    }
}
