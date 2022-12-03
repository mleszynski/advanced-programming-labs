package ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.*;
import services.*;
import result.*;
import request.*;

public class AllEventSerTest {
    private RegisterService registerService;
    private FillService fillService;
    private AllEventService allEventService;

    @BeforeEach
    public void setUp() {
        this.registerService = new RegisterService();
        this.fillService = new FillService();
        this.allEventService = new AllEventService();
    }

    @AfterEach
    public void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void allEventPass() {
        // Registers a new user and fills the tree, then pulls all events.
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
        AllEventResult eventResult = allEventService.allEvents(authtoken);
        Event[] events = eventResult.getData();
        assertEquals(91, events.length);
    }

    @Test
    public void allEventFail() {
        // tries to pull events for a non-existing authtoken.
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
        FillResult fillResult = fillService.fill(tempUser.getUsername(), 4);
        assertTrue(fillResult.isSuccess());
        AllEventResult eventResult = allEventService.allEvents("THISTOKENISWRONG");
        assertFalse(eventResult.isSuccess());
        assertEquals("Error: No such authtoken found", eventResult.getMessage());
    }
}
