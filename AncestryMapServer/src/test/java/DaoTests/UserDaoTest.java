package DaoTests;

import java.sql.Connection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.*;
import dao.*;

public class UserDaoTest {
    private Database db;
    private Connection conn;
    private UserDao userDao;
    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() throws DataAccessException {
        this.db = new Database();
        this.user1 = new User("mleszynski", "thisismypassword", "anemail@gmail.com",
                "Marc", "Lesser", "m", "anID123");
        this.user2 = new User("badmleszynski", "thisismypassword", "anemail@gmail.com",
                "Marc", "Lesser", "m", "anID321");
        this.conn = db.openConnection();
        this.userDao = new UserDao(conn);
        this.userDao.clear();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        this.db.closeConnection(true);
    }

    @Test
    public void insertPass() throws DataAccessException {
        // add a user and confirm data addition to database.
        userDao.insert(user1);
        User tempUser = userDao.find(user1.getUsername());
        assertNotNull(tempUser);
        assertEquals(user1, tempUser);
    }

    @Test
    public void insertFail() throws DataAccessException {
        // tries to add an already existing user.
        userDao.insert(user1);
        assertThrows(DataAccessException.class, ()-> userDao.insert(user1));
    }

    @Test
    public void findPass() throws DataAccessException {
        userDao.insert(user1);
        userDao.insert(user2);

        // fetch and test 1st user.
        User tempUser1 = userDao.find(user1.getUsername());
        assertNotNull(tempUser1);
        assertEquals(user1, tempUser1);

        // fetch and test 2nd user.
        User tempUser2 = userDao.find(user2.getUsername());
        assertNotNull(tempUser2);
        assertEquals(user2, tempUser2);

        // compares both users in table.
        assertNotEquals(tempUser1, tempUser2);
    }

    @Test
    public void findFail() throws DataAccessException {
        // adds one user and looks for 2nd, un-added user.
        userDao.insert(user1);
        assertNull(userDao.find(user2.getUsername()));
    }

    @Test
    public void clearPass() throws DataAccessException {
        // adds two users, clears the table, and checks that the users aren't there.
        userDao.insert(user1);
        userDao.insert(user2);
        userDao.clear();
        assertNull(userDao.find(user1.getUsername()));
        assertNull(userDao.find(user2.getUsername()));
    }

    @Test
    public void removeUserPass() throws DataAccessException {
        // adds two users, removes them one by one, and verifies removal.
        userDao.insert(user1);
        userDao.insert(user2);
        userDao.removeUser(user1.getUsername());
        assertNull(userDao.find(user1.getUsername()));
        userDao.removeUser(user2.getUsername());
        assertNull(userDao.find(user2.getUsername()));
    }

    @Test
    public void removeUserMultiple() throws DataAccessException {
        // adds a user, and then attempts to remove that user multiple times.
        userDao.insert(user1);
        userDao.removeUser(user1.getUsername());
        userDao.removeUser(user1.getUsername());
        assertNull(userDao.find(user1.getUsername()));
    }

    @Test
    public void isUserFoundPass() throws DataAccessException {
        // adds two users and tries to find both of them.
        userDao.insert(user1);
        userDao.insert(user2);
        assertTrue(userDao.isUserFound(user1.getUsername()));
        assertTrue(userDao.isUserFound(user2.getUsername()));
    }

    @Test
    public void isUserFoundFail() throws DataAccessException {
        // adds two users, clears the table, and asserts that neither user is found.
        userDao.insert(user1);
        userDao.insert(user2);
        userDao.clear();
        assertFalse(userDao.isUserFound(user1.getUsername()));
        assertFalse(userDao.isUserFound(user2.getUsername()));
    }
}
