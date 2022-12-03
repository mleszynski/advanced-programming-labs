package ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.*;
import services.*;
import result.*;
import request.*;

public class ClearSerTest {
    private RegisterService registerService;
    private ClearService clearService;

    @BeforeEach
    public void setUp() {
        this.registerService = new RegisterService();
        this.clearService = new ClearService();
    }

    @AfterEach
    public void tearDown() {
        clearService.clear();
    }

    @Test
    public void clearPass() {
        // Registers a new user, clears the database, and confirms the clear.
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
        ClearResult clearResult = clearService.clear();
        assertTrue(clearResult.isSuccess());
        assertEquals("Clear succeeded.", clearResult.getMessage());
    }
}
