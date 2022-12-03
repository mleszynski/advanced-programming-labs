package DaoTests;

import java.sql.Connection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.*;
import dao.*;

public class EventDaoTest {
    private Database db;
    private Connection conn;
    private EventDao eventDao;
    private Event event1;
    private Event event2;

    @BeforeEach
    public void setUp() throws DataAccessException {
        this.db = new Database();
        this.event1 = new Event("goodEVENT123", "mleszynski", "anID123",
                81.6f, -15.3333f, "Denmark", "Nord", "birth",
                1996);
        this.event2 = new Event("badEVENT123", "mleszynski", "anID123",
                81.6f, -15.3333f, "Denmark", "Nord", "mission",
                2014);
        this.conn = db.openConnection();
        this.eventDao = new EventDao(conn);
        this.eventDao.clear();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        this.db.closeConnection(true);
    }

    @Test
    public void insertPass() throws DataAccessException {
        // add an event and confirm data addition to database.
        eventDao.insert(event1);
        Event tempEvent = eventDao.find(event1.getEventID());
        assertNotNull(tempEvent);
        assertEquals(event1, tempEvent);
    }

    @Test
    public void insertFail() throws DataAccessException {
        // tries to add an already existing event.
        eventDao.insert(event1);
        assertThrows(DataAccessException.class, ()-> eventDao.insert(event1));
    }

    @Test
    public void findPass() throws DataAccessException {
        eventDao.insert(event1);
        eventDao.insert(event2);

        // fetch and test 1st event.
        Event tempEvent1 = eventDao.find(event1.getEventID());
        assertNotNull(tempEvent1);
        assertEquals(event1, tempEvent1);

        // fetch and test 2nd event.
        Event tempEvent2 = eventDao.find(event2.getEventID());
        assertNotNull(tempEvent2);
        assertEquals(event2, tempEvent2);

        // compares both events in table.
        assertNotEquals(tempEvent1, tempEvent2);
    }

    @Test
    public void findFail() throws DataAccessException {
        // adds one event and looks for 2nd, un-added event.
        eventDao.insert(event1);
        assertNull(eventDao.find(event2.getEventID()));
    }

    @Test
    public void findAllPass() throws DataAccessException {
        // adds two events to the database, then pulls both with a findAll call.
        eventDao.insert(event1);
        eventDao.insert(event2);
        Event[] tempList = eventDao.findAll(event1.getAssociatedUsername());
        assertNotNull(tempList);
        assertEquals(2, tempList.length);
        assertEquals(eventDao.find(event1.getEventID()), tempList[0]);
        assertEquals(eventDao.find(event2.getEventID()), tempList[1]);
    }

    @Test
    public void findAllFail() throws DataAccessException {
        // adds two events to the database, clears the table, and asserts no events found.
        eventDao.insert(event1);
        eventDao.insert(event2);
        Event[] tempList = eventDao.findAll(event1.getAssociatedUsername());
        assertNotNull(tempList);
        assertEquals(2, tempList.length);
        eventDao.clear();
        tempList = eventDao.findAll(event1.getAssociatedUsername());
        assertEquals(0, tempList.length);
    }
    
    @Test
    public void clearPass() throws DataAccessException {
        // adds two events, clears the table, and checks that the events aren't there.
        eventDao.insert(event1);
        eventDao.insert(event2);
        eventDao.clear();
        assertNull(eventDao.find(event1.getEventID()));
        assertNull(eventDao.find(event2.getEventID()));
    }
    
    @Test
    public void removeUserPass1() throws DataAccessException {
        // adds two events with the same user and verifies removal.
        eventDao.insert(event1);
        eventDao.insert(event2);
        assertEquals(event1.getAssociatedUsername(), event2.getAssociatedUsername());
        eventDao.removeUserEvents(event1.getAssociatedUsername());
        assertNull(eventDao.find(event1.getEventID()));
    }

    @Test
    public void removeUserPass2() throws DataAccessException {
        // same as removeUserPass1(), but removes using second event's username.
        eventDao.insert(event1);
        eventDao.insert(event2);
        assertEquals(event1.getAssociatedUsername(), event2.getAssociatedUsername());
        eventDao.removeUserEvents(event2.getAssociatedUsername());
        assertNull(eventDao.find(event1.getEventID()));
    }

    @Test
    public void isEventFoundPass() throws DataAccessException {
        // adds two events and tries to find both of them.
        eventDao.insert(event1);
        eventDao.insert(event2);
        assertTrue(eventDao.isEventFound(event1.getEventID()));
        assertTrue(eventDao.isEventFound(event2.getEventID()));
    }

    @Test
    public void isEventFoundFail() throws DataAccessException {
        // adds two events, clears the table, and asserts that neither event is found.
        eventDao.insert(event1);
        eventDao.insert(event2);
        eventDao.clear();
        assertFalse(eventDao.isEventFound(event1.getEventID()));
        assertFalse(eventDao.isEventFound(event2.getEventID()));
    }
}
