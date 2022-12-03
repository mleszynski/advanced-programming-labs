package ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.*;
import services.*;
import result.*;
import request.*;

public class LoginSerTest {
    private RegisterService registerService;
    private LoginService loginService;

    @BeforeEach
    public void setUp() {
        this.registerService = new RegisterService();
        this.loginService = new LoginService();
    }

    @AfterEach
    public void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void loginPass() {
        // successfully logs a user in.
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
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(tempUser.getUsername());
        loginRequest.setPassword(tempUser.getPassword());
        LoginResult loginResult = loginService.login(loginRequest);
        assertTrue(loginResult.isSuccess());
        assertEquals(result.getUsername(), loginResult.getUsername());
    }

    @Test
    public void loginFail() {
        // tries to log in an unregistered user.
        User tempUser = new User("mleszynski", "thisismypass", "temp@gmail.com",
                "Marc", "Lesser", "m", "tempID123");
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(tempUser.getUsername());
        loginRequest.setPassword(tempUser.getPassword());
        LoginResult loginResult = loginService.login(loginRequest);
        assertFalse(loginResult.isSuccess());
        assertEquals("Error: No such username or password", loginResult.getMessage());
    }
}
