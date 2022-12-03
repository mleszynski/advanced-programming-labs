package ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.*;
import services.*;
import result.*;
import request.*;

public class FillSerTest {
    private RegisterService registerService;
    private FillService fillService;

    @BeforeEach
    public void setUp() {
        this.registerService = new RegisterService();
        this.fillService = new FillService();
    }

    @AfterEach
    public void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void fillPass() {
        // successfully fills the user's tree for four generations.
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
        assertEquals("Successfully added 31 persons and 91 events to the database.", fillResult.getMessage());
    }

    @Test
    public void fillFail() {
        // tries to fill the user's tree with a nonexisting user.
        User tempUser = new User("mleszynski", "thisismypass", "temp@gmail.com",
                "Marc", "Lesser", "m", "tempID123");
        FillResult fillResult = fillService.fill(tempUser.getUsername(), 4);
        assertFalse(fillResult.isSuccess());
        assertEquals("Error: Incorrect User or Generations parameter", fillResult.getMessage());
    }
}
