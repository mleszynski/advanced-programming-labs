package dao;

import java.sql.*;
import model.Authtoken;

/**
 * Performs all operations on a database associated with the Authtoken table.
 */
public class AuthtokenDao {
    /**
     * Connection belonging to a given instance.
     */
    private Connection conn;

    public AuthtokenDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Removes all data from Authtoken table.
     * @throws DataAccessException in case of database error.
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DROP TABLE IF EXISTS Authtoken;";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE Authtoken (" +
                    "authtoken TEXT NOT NULL UNIQUE, " +
                    "username TEXT NOT NULL, " +
                    "PRIMARY KEY(authtoken));";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to reload Authtoken table");
        }
    }

    /**
     * Adds a new Authtoken to the database.
     * @param newToken Authtoken to add to the database.
     * @throws DataAccessException in case of database error.
     */
    public void insert(Authtoken newToken) throws DataAccessException {
        String sql = "INSERT INTO Authtoken (authtoken, username) " +
                "VALUES(?,?);";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newToken.getAuthtoken());
            stmt.setString(2, newToken.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to insert into Authtoken table");
        }
    }

    /**
     * Checks to see if token exists in the database.
     * @param token String value of token to validate.
     * @return Authtoken object if found in database, null otherwise.
     * @throws DataAccessException in case of database error.
     */
    public Authtoken find(String token) throws DataAccessException {
        Authtoken authtoken;
        ResultSet rs = null;
        String sql = "SELECT * FROM Authtoken WHERE authtoken = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authtoken = new Authtoken(rs.getString("authtoken"),
                                          rs.getString("username"));
                return authtoken;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to find token in Authtoken table");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Removes all authtokens from database that are associated with the given username.
     * @param associatedUsername user whose persons we are removing from the database.
     */
    public void removeUserTokens(String associatedUsername) throws DataAccessException {
        String sql = "DELETE FROM Authtoken WHERE username = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to remove all corresponding tokens in Authtoken table");
        }
    }

    /**
     * Checks the Authtoken table and returns a boolean representing whether a given
     * token is contained in the table.
     * @param token String value of token to find
     * @return true if token is found in database, false otherwise.
     * @throws DataAccessException in case of database error.
     */
    public boolean isTokenFound(String token) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM Authtoken WHERE authtoken = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Checks the Authtoken table and returns a boolean representing whether a given
     * username is contained in the table.
     * @param username String value of token to find
     * @return true if username is found in database, false otherwise.
     * @throws DataAccessException in case of database error.
     */
    public boolean isUsernameFound(String username) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM Authtoken WHERE username = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Helper function for login procedure, updates a User's associated Authtoken.
     * @param newToken New Authtoken string to be associated with the user.
     * @param username User whose token is to be updated.
     * @throws DataAccessException in case of database error.
     */
    public void updateToken(String newToken, String username) throws DataAccessException {
        String sql = "UPDATE Authtoken SET authtoken = ? WHERE username = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newToken);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to update token in Authtoken table");
        }
    }
}
