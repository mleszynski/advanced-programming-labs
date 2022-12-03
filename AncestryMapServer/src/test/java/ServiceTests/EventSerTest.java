package ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.*;
import services.*;
import result.*;
import request.*;

public class EventSerTest {
    private RegisterService registerService;
    private FillService fillService;
    private EventService eventService;

    @BeforeEach
    public void setUp() {
        this.registerService = new RegisterService();
        this.fillService = new FillService();
        this.eventService = new EventService();
    }

    @AfterEach
    public void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void eventPass() {
        // Registers a new user and fills the tree, then pulls an event.
        User tempUser = new User("mleszynski", "thisismypass", "temp@gmail.com",
                "Marc", "Lesser", "m", "tempID123");
        RegisterRequest request = new RegisterRequest();
        request.setUsername(tempUser.getUsername());
        request.setPassword(tempUser.getPassword());
        request.setEmail(tempUser.getEmail());
        request.setFirstName(tempUser.getFirstName());
        request.setLastName(tempUser.getLastName());
        request.setGender(tempUser.getGender());
        RegisterResult result = registerService.register(request);
        assertTrue(result.isSuccess());
        String authtoken = result.getAuthtoken();
        FillResult fillResult = fillService.fill(tempUser.getUsername(), 4);
        assertTrue(fillResult.isSuccess());
        AllEventService allEventService = new AllEventService();
        AllEventResult allEventResult = allEventService.allEvents(authtoken);
        assertTrue(allEventResult.isSuccess());
        Event[] events = allEventResult.getData();
        String eventID = events[0].getEventID();
        EventResult eventResult = eventService.oneEvent(eventID, authtoken);
        assertTrue(eventResult.isSuccess());
        assertEquals(eventID, eventResult.getEventID());
    }

    @Test
    public void eventFail() {
        // Registers a new user and fills the tree, then pulls an event with a wrong ID.
        User tempUser = new User("mleszynski", "thisismypass", "temp@gmail.com",
                "Marc", "Lesser", "m", "tempID123");
        RegisterRequest request = new RegisterRequest();
        request.setUsername(tempUser.getUsername());
        request.setPassword(tempUser.getPassword());
        request.setEmail(tempUser.getEmail());
        request.setFirstName(tempUser.getFirstName());
        request.setLastName(tempUser.getLastName());
        request.setGender(tempUser.getGender());
        RegisterResult result = registerService.register(request);
        assertTrue(result.isSuccess());
        String authtoken = result.getAuthtoken();
        FillResult fillResult = fillService.fill(tempUser.getUsername(), 4);
        assertTrue(fillResult.isSuccess());
        EventResult eventResult = eventService.oneEvent("THISISWRONG", authtoken);
        assertFalse(eventResult.isSuccess());
        assertEquals("Error: No match for eventID found", eventResult.getMessage());
    }
}
