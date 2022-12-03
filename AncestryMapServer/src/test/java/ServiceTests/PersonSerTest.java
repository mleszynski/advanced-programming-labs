package ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.*;
import services.*;
import result.*;
import request.*;

public class PersonSerTest {
    private RegisterService registerService;
    private FillService fillService;
    private PersonService personService;

    @BeforeEach
    public void setUp() {
        this.registerService = new RegisterService();
        this.fillService = new FillService();
        this.personService = new PersonService();
    }

    @AfterEach
    public void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void personPass() {
        // Registers a new user and fills the tree, then pulls a person.
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
        AllPersonService allPersonService = new AllPersonService();
        AllPersonResult allPersonResult = allPersonService.allPersons(authtoken);
        assertTrue(allPersonResult.isSuccess());
        Person[] persons = allPersonResult.getData();
        String personID = persons[0].getPersonID();
        PersonResult personResult = personService.onePerson(personID, authtoken);
        assertTrue(personResult.isSuccess());
        assertEquals(personID, personResult.getPersonID());
    }

    @Test
    public void personFail() {
        // Registers a new user and fills the tree, then pulls a person with a wrong ID.
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
        PersonResult personResult = personService.onePerson("THISISWRONG", authtoken);
        assertFalse(personResult.isSuccess());
        assertEquals("Error: No such personID found", personResult.getMessage());
    }
}
