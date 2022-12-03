package DaoTests;

import java.sql.Connection;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.*;
import dao.*;

public class PersonDaoTest {
    private Database db;
    private Connection conn;
    private PersonDao personDao;
    private UserDao userDao;
    private EventDao eventDao;
    private Person person1;
    private Person person2;

    @BeforeEach
    public void setUp() throws DataAccessException {
        this.db = new Database();
        this.person1 = new Person("thisID123", "mleszynski", "Marc", "Lesser",
                "m", "dadID123", "momID123", "spouseID123");
        this.person2 = new Person("thisID321", "mleszynski", "Ethan", "Lesser",
                "m", "dadID123", "momID123", "spouseID123");
        this.conn = db.openConnection();
        this.personDao = new PersonDao(conn);
        this.userDao = new UserDao(conn);
        this.eventDao = new EventDao(conn);
        this.personDao.clear();
        this.userDao.clear();
        this.eventDao.clear();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(true);
    }

    @Test
    public void insertPass() throws DataAccessException {
        personDao.insert(person1);
        Person tempPerson = personDao.find(person1.getPersonID());
        assertNotNull(tempPerson);
        assertEquals(person1, tempPerson);
    }

    @Test
    public void insertFail() throws DataAccessException {
        // tries to add an already existing person.
        personDao.insert(person1);
        assertThrows(DataAccessException.class, ()-> personDao.insert(person1));
    }

    @Test
    public void findPass() throws DataAccessException {
        personDao.insert(person1);
        personDao.insert(person2);

        // fetch and test 1st person.
        Person tempPerson1 = personDao.find(person1.getPersonID());
        assertNotNull(tempPerson1);
        assertEquals(person1, tempPerson1);

        // fetch and test 2nd person.
        Person tempPerson2 = personDao.find(person2.getPersonID());
        assertNotNull(tempPerson2);
        assertEquals(person2, tempPerson2);

        // compares both persons in table.
        assertNotEquals(tempPerson1, tempPerson2);
    }

    @Test
    public void findFail() throws DataAccessException {
        // adds one person and looks for 2nd, un-added person.
        personDao.insert(person1);
        assertNull(personDao.find(person2.getPersonID()));
    }

    @Test
    public void findAllPass() throws DataAccessException {
        // adds two persons to the database, then pulls both with a findAll call.
        personDao.insert(person1);
        personDao.insert(person2);
        Person[] tempList = personDao.findAll(person1.getAssociatedUsername());
        assertNotNull(tempList);
        assertEquals(2, tempList.length);
        assertEquals(personDao.find(person1.getPersonID()), tempList[0]);
        assertEquals(personDao.find(person2.getPersonID()), tempList[1]);
    }

    @Test
    public void findAllFail() throws DataAccessException {
        // adds two persons to the database, clears the table, and asserts no persons found.
        personDao.insert(person1);
        personDao.insert(person2);
        Person[] tempList = personDao.findAll(person1.getAssociatedUsername());
        assertNotNull(tempList);
        assertEquals(2, tempList.length);
        personDao.clear();
        tempList = personDao.findAll(person1.getAssociatedUsername());
        assertEquals(0, tempList.length);
    }

    @Test
    public void clearPass() throws DataAccessException {
        // adds two persons, clears the table, and checks that the persons aren't there.
        personDao.insert(person1);
        personDao.insert(person2);
        personDao.clear();
        assertNull(personDao.find(person1.getPersonID()));
        assertNull(personDao.find(person2.getPersonID()));
    }

    @Test
    public void removeUserPass1() throws DataAccessException {
        // adds two persons with the same user and verifies removal.
        personDao.insert(person1);
        personDao.insert(person2);
        assertEquals(person1.getAssociatedUsername(), person2.getAssociatedUsername());
        personDao.removeUserPersons(person1.getAssociatedUsername());
        assertNull(personDao.find(person1.getPersonID()));
    }

    @Test
    public void removeUserPass2() throws DataAccessException {
        // same as removeUserPass1(), but removes using second person's username.
        personDao.insert(person1);
        personDao.insert(person2);
        assertEquals(person1.getAssociatedUsername(), person2.getAssociatedUsername());
        personDao.removeUserPersons(person2.getAssociatedUsername());
        assertNull(personDao.find(person1.getPersonID()));
    }

    @Test
    public void isPersonFoundPass() throws DataAccessException {
        // adds two persons and tries to find both of them.
        personDao.insert(person1);
        personDao.insert(person2);
        assertTrue(personDao.isPersonFound(person1.getPersonID()));
        assertTrue(personDao.isPersonFound(person2.getPersonID()));
    }

    @Test
    public void isPersonFoundFail() throws DataAccessException {
        // adds two persons, clears the table, and asserts that neither person is found.
        personDao.insert(person1);
        personDao.insert(person2);
        personDao.clear();
        assertFalse(personDao.isPersonFound(person1.getPersonID()));
        assertFalse(personDao.isPersonFound(person2.getPersonID()));
    }

    @Test
    public void updateFatherPass() throws DataAccessException {
        // adds two persons, then changes the dad of person1 to match person2.
        personDao.insert(person1);
        personDao.insert(person2);
        personDao.updateFather(person1.getPersonID(), person2.getFatherID());
        assertEquals(person2.getFatherID(), person1.getFatherID());
    }

    @Test
    public void updateFatherFail() throws DataAccessException {
        // adds two persons, changes the dad of person1, checks person2's dad is unchanged.
        personDao.insert(person1);
        personDao.insert(person2);
        String tempID = "newID123";
        personDao.updateFather(person1.getPersonID(), tempID);
        assertNotEquals(tempID, person2.getFatherID());
    }

    @Test
    public void updateMotherPass() throws DataAccessException {
        // adds two persons, then changes the mom of person1 to match person2.
        personDao.insert(person1);
        personDao.insert(person2);
        personDao.updateMother(person1.getPersonID(), person2.getMotherID());
        assertEquals(person2.getMotherID(), person1.getMotherID());
    }

    @Test
    public void updateMotherFail() throws DataAccessException {
        // adds two persons, changes the mom of person1, checks person2's mom is unchanged.
        personDao.insert(person1);
        personDao.insert(person2);
        String tempID = "newID123";
        personDao.updateMother(person1.getPersonID(), tempID);
        assertNotEquals(tempID, person2.getMotherID());
    }

    @Test
    public void makeRoot0Gens() throws DataAccessException {
        // generates a root user and adds zero generations of parents.
        String personID = UUID.randomUUID().toString();
        User user1 = new User("mleszynski", "thisismypass", "temp@google.com",
                "Marc", "Lesser", "m", personID);
        userDao.insert(user1);
        personDao.makeRoot(eventDao, user1, personID, 0);
        assertNull(personDao.find(personID).getFatherID());
        assertNull(personDao.find(personID).getMotherID());
    }

    @Test
    public void makeRoot1Gens() throws DataAccessException {
        // generates a root user and adds one generation of parents.
        // checks that root user has parents but no grandparents.
        String personID = UUID.randomUUID().toString();
        User user1 = new User("mleszynski", "thisismypass", "temp@google.com",
                "Marc", "Lesser", "m", personID);
        userDao.insert(user1);
        personDao.makeRoot(eventDao, user1, personID, 1);
        assertNotNull(personDao.find(personID).getFatherID());
        String fatherID = personDao.find(personID).getFatherID();
        assertNotNull(personDao.find(personID).getMotherID());
        String motherID = personDao.find(personID).getMotherID();
        assertNull(personDao.find(fatherID).getFatherID());
        assertNull(personDao.find(fatherID).getMotherID());
        assertNull(personDao.find(motherID).getFatherID());
        assertNull(personDao.find(motherID).getMotherID());
    }
}
