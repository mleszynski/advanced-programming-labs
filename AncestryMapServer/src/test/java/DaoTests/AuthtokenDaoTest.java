package DaoTests;

import java.sql.Connection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.*;
import dao.*;

public class AuthtokenDaoTest {
    private Database db;
    private Connection conn;
    private AuthtokenDao authDao;
    private Authtoken token1;
    private Authtoken token2;
    private Authtoken token3;

    @BeforeEach
    public void setUp() throws DataAccessException {
        this.db = new Database();
        this.token1 = new Authtoken("tokenFIRST123", "mleszynski");
        this.token2 = new Authtoken("tokenSECOND123", "mleszynski");
        this.token3 = new Authtoken("tokenTHIRD123", "leszynski200");
        this.conn = db.openConnection();
        this.authDao = new AuthtokenDao(conn);
        this.authDao.clear();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        this.db.closeConnection(true);
    }

    @Test
    public void insertPass() throws DataAccessException {
        // add a token and confirm data addition to database.
        authDao.insert(token1);
        Authtoken tempToken = authDao.find(token1.getAuthtoken());
        assertNotNull(tempToken);
        assertEquals(token1, tempToken);
    }

    @Test
    public void insertFail() throws DataAccessException {
        // tries to add an already existing token.
        authDao.insert(token1);
        assertThrows(DataAccessException.class, ()-> authDao.insert(token1));
    }

    @Test
    public void findPass() throws DataAccessException {
        authDao.insert(token1);
        authDao.insert(token2);

        // fetch and test 1st token.
        Authtoken tempToken1 = authDao.find(token1.getAuthtoken());
        assertNotNull(tempToken1);
        assertEquals(token1, tempToken1);

        // fetch and test 2nd token.
        Authtoken tempToken2 = authDao.find(token2.getAuthtoken());
        assertNotNull(tempToken2);
        assertEquals(token2, tempToken2);

        // compares both tokens in table.
        assertNotEquals(tempToken1, tempToken2);
    }

    @Test
    public void findFail() throws DataAccessException {
        // adds one token and looks for 2nd, un-added token.
        authDao.insert(token1);
        assertNull(authDao.find(token2.getAuthtoken()));
    }

    @Test
    public void clearPass() throws DataAccessException {
        // adds two tokens, clears the table, and checks that the tokens aren't there.
        authDao.insert(token1);
        authDao.insert(token2);
        authDao.clear();
        assertNull(authDao.find(token1.getAuthtoken()));
        assertNull(authDao.find(token2.getAuthtoken()));
    }

    @Test
    public void removeUserPass1() throws DataAccessException {
        // adds two tokens with the same user and verifies removal.
        authDao.insert(token1);
        authDao.insert(token2);
        assertEquals(token1.getUsername(), token2.getUsername());
        authDao.removeUserTokens(token1.getUsername());
        assertNull(authDao.find(token1.getAuthtoken()));
    }

    @Test
    public void removeUserPass2() throws DataAccessException {
        // same as removeUserPass1(), but removes using second token's username.
        authDao.insert(token1);
        authDao.insert(token2);
        assertEquals(token1.getUsername(), token2.getUsername());
        authDao.removeUserTokens(token2.getUsername());
        assertNull(authDao.find(token1.getAuthtoken()));
    }

    @Test
    public void isTokenFoundPass() throws DataAccessException {
        // adds two tokens and tries to find both of them.
        authDao.insert(token1);
        authDao.insert(token2);
        assertTrue(authDao.isTokenFound(token1.getAuthtoken()));
        assertTrue(authDao.isTokenFound(token2.getAuthtoken()));
    }

    @Test
    public void isTokenFoundFail() throws DataAccessException {
        // adds two tokens, clears the table, and asserts that neither token is found.
        authDao.insert(token1);
        authDao.insert(token2);
        authDao.clear();
        assertFalse(authDao.isTokenFound(token1.getAuthtoken()));
        assertFalse(authDao.isTokenFound(token2.getAuthtoken()));
    }

    @Test
    public void isUserFoundPass() throws DataAccessException {
        // adds two tokens and tries to find both of them.
        authDao.insert(token1);
        authDao.insert(token2);
        assertTrue(authDao.isUsernameFound(token1.getUsername()));
        assertTrue(authDao.isUsernameFound(token2.getUsername()));
    }

    @Test
    public void isUserFoundFail() throws DataAccessException {
        // adds two tokens, clears the table, and asserts that neither username is found.
        authDao.insert(token1);
        authDao.insert(token2);
        authDao.clear();
        assertFalse(authDao.isUsernameFound(token1.getUsername()));
        assertFalse(authDao.isUsernameFound(token2.getUsername()));
    }

    @Test
    public void updateTokenPass1() throws DataAccessException {
        // adds a token object, then updates the object's authtoken value.
        authDao.insert(token1);
        assertEquals("tokenFIRST123", token1.getAuthtoken());
        authDao.updateToken("tokenFIRST321", token1.getUsername());
        Authtoken tempToken = authDao.find("tokenFIRST321");
        assertNotNull(tempToken);
        assertEquals("tokenFIRST321", tempToken.getAuthtoken());
    }

    @Test
    public void updateTokenPass2() throws DataAccessException {
        // same as updateTokenPass1(), but updates in a database with multiple tokens.
        authDao.insert(token1);
        assertEquals("tokenFIRST123", token1.getAuthtoken());
        authDao.insert(token3);
        authDao.updateToken("tokenFIRST321", token1.getUsername());
        Authtoken tempToken = authDao.find("tokenFIRST321");
        assertNotNull(tempToken);
        assertEquals("tokenFIRST321", tempToken.getAuthtoken());
    }
}
