package ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.*;
import services.*;
import result.*;
import request.*;

public class AllPersonSerTest {
    private RegisterService registerService;
    private FillService fillService;
    private AllPersonService allPersonService;

    @BeforeEach
    public void setUp() {
        this.registerService = new RegisterService();
        this.fillService = new FillService();
        this.allPersonService = new AllPersonService();
    }

    @AfterEach
    public void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void allPersonPass() {
        // Registers a new user and fills the tree, then pulls all persons.
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
        AllPersonResult personResult = allPersonService.allPersons(authtoken);
        assertTrue(personResult.isSuccess());
        Person[] personList = personResult.getData();
        assertEquals(31, personList.length);
    }

    @Test
    public void allPersonFail() {
        // Registers a new user and fills the tree, then pulls all persons.
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
        AllPersonResult personResult = allPersonService.allPersons("THISTOKENISWRONG");
        assertFalse(personResult.isSuccess());
        assertEquals("Error: No such authtoken found", personResult.getMessage());
    }
}
