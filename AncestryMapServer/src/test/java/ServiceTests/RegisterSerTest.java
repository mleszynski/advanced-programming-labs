package ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.*;
import services.*;
import result.*;
import request.*;

public class RegisterSerTest {
    private RegisterService registerService;

    @BeforeEach
    public void setUp() {
        this.registerService = new RegisterService();
    }

    @AfterEach
    public void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void registerPass() {
        // tests successful registration of a new user.
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
        assertEquals(tempUser.getUsername(), result.getUsername());
    }

    @Test
    public void registerFail() {
        // tries to register a user with an invalid gender value.
        User tempUser = new User("mleszynski", "thisismypass", "temp@gmail.com",
                "Marc", "Lesser", "asdfsadf", "tempID123");
        RegisterRequest request = new RegisterRequest();
        request.setUsername(tempUser.getUsername());
        request.setPassword(tempUser.getPassword());
        request.setEmail(tempUser.getEmail());
        request.setFirstName(tempUser.getFirstName());
        request.setLastName(tempUser.getLastName());
        request.setGender(tempUser.getGender());
        RegisterResult result = registerService.register(request);
        assertFalse(result.isSuccess());
        assertEquals("Error: Registration values are missing or invalid.", result.getMessage());
    }
}
