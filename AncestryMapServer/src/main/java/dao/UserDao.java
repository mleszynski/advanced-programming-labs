package dao;

import java.sql.*;
import model.User;

/**
 * Performs all operations on a database associated with the User table.
 */
public class UserDao {
    /**
     * Connection belonging to a given instance.
     */
    private Connection conn;

    /**
     * Keeps track of number of users added by Dao, used in services.
     */
    private int numUsers;

    public UserDao(Connection conn) {
        this.conn = conn;
        this.numUsers = 0;
    }

    public int getNumUsers() {
        return numUsers;
    }

    /**
     * Removes all data from User table.
     * @throws DataAccessException in case of database error.
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DROP TABLE IF EXISTS User;";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE User (" +
                    "username TEXT NOT NULL UNIQUE, " +
                    "password TEXT NOT NULL, " +
                    "email TEXT NOT NULL, " +
                    "firstName TEXT NOT NULL, " +
                    "lastName TEXT NOT NULL, " +
                    "gender TEXT NOT NULL, " +
                    "personID TEXT NOT NULL, " +
                    "PRIMARY KEY(username));";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to reload User table");
        }

        //Example code
//        String sql = "DELETE FROM User";
//        try(PreparedStatement stmt = this.conn.prepareStatement(sql)) {
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DataAccessException("Database Error: failed to clear User table");
//        }
    }

    /**
     * Adds a new user to the database.
     * @param newUser User to add to the database.
     * @throws DataAccessException in case of database error.
     */
    public void insert(User newUser) throws DataAccessException {
        String sql = "INSERT INTO User (username, password, email, " +
                "firstName, lastName, gender, personID) VALUES(?,?,?,?,?,?,?);";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newUser.getUsername());
            stmt.setString(2, newUser.getPassword());
            stmt.setString(3, newUser.getEmail());
            stmt.setString(4, newUser.getFirstName());
            stmt.setString(5, newUser.getLastName());
            stmt.setString(6, newUser.getGender());
            stmt.setString(7, newUser.getPersonID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to insert into User table");
        }
        numUsers++;
    }

    /**
     * Checks to see if user exists in the database.
     * @param username String value of username to find.
     * @return User object if found in database, null otherwise.
     * @throws DataAccessException in case of database error.
     */
    public User find(String username) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM User WHERE username = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("email"),
                                rs.getString("firstName"),
                                rs.getString("lastName"),
                                rs.getString("gender"),
                                rs.getString("personID"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to find user in User table");
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
     * Removes a user from the database.
     * @param username String value of username to find.
     * @throws DataAccessException in case of database error.
     */
    public void removeUser(String username) throws DataAccessException {
        String sql = "DELETE FROM User WHERE username = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to remove user from User table");
        }
    }

    /**
     * Checks the User table and returns a boolean representing whether a given
     * username is contained in the table.
     * @param username String value of username to find
     * @return true if username is found in database, false otherwise.
     * @throws DataAccessException in case of database error.
     */
    public boolean isUserFound(String username) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM User WHERE username = ?;";

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
}
