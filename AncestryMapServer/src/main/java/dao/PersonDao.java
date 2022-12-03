package dao;

import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import model.*;
import encodeDecode.*;

/**
 * Performs all operations on a database associated with the Person table.
 */
public class PersonDao {
    /**
     * Connection belonging to a given instance.
     */
    private Connection conn;

    /**
     * List of male names read in from JSON file used for person generation.
     */
    private ArrayList<String> maleNames;

    /**
     * List of female names read in from JSON file used for person generation.
     */
    private ArrayList<String> femaleNames;

    /**
     * List of surnames read in from JSON file used for person generation.
     */
    private ArrayList<String> surNames;

    /**
     * Keeps track of number of people added by Dao, used in services.
     */
    private int numPeople;

    /**
     * Constructor which establishes a connection and reads in name data from
     * provided JSON files.
     * @param conn Connection belonging to a given instance.
     */
    public PersonDao(Connection conn) {
        this.conn = conn;
        this.numPeople = 0;

        try {
            NamesList maleNames = Decoder.decodeNames(new File("json/mnames.json"));
            this.maleNames = new ArrayList<String>(Arrays.asList(maleNames.getNames()));
            NamesList femaleNames = Decoder.decodeNames(new File("C:\\Users\\Marcelo\\IdeaProjects\\FamilyMapServer\\json\\fnames.json"));
            this.femaleNames = new ArrayList<String>(Arrays.asList(femaleNames.getNames()));
            NamesList surNames = Decoder.decodeNames(new File("json\\snames.json"));
            this.surNames = new ArrayList<String>(Arrays.asList(surNames.getNames()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumPeople() {
        return numPeople;
    }

    /**
     * Removes all data from Person table.
     * @throws DataAccessException in case of database error.
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DROP TABLE IF EXISTS Person;";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE Person (" +
                    "personID TEXT NOT NULL UNIQUE, " +
                    "username TEXT NOT NULL, " +
                    "firstName TEXT NOT NULL, " +
                    "lastName TEXT NOT NULL, " +
                    "gender TEXT NOT NULL, " +
                    "fatherID TEXT, " +
                    "motherID TEXT, " +
                    "spouseID TEXT, " +
                    "PRIMARY KEY(personID));";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to reload Person table");
        }
    }

    /**
     * Adds a new person to the database.
     * @param newPerson Person to add to the database.
     * @throws DataAccessException in case of database error.
     */
    public void insert(Person newPerson) throws DataAccessException {
        String sql = "INSERT INTO Person (personID, username, firstName, lastName, " +
                "gender, fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?);";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPerson.getPersonID());
            stmt.setString(2, newPerson.getAssociatedUsername());
            stmt.setString(3, newPerson.getFirstName());
            stmt.setString(4, newPerson.getLastName());
            stmt.setString(5, newPerson.getGender());
            stmt.setString(6, newPerson.getFatherID());
            stmt.setString(7, newPerson.getMotherID());
            stmt.setString(8, newPerson.getSpouseID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to insert into Person table");
        }
        numPeople++;
    }

    /**
     * Checks to see if person exists in the database.
     * @param personID String value of personID to find.
     * @return Person object if found in database, null otherwise.
     * @throws DataAccessException in case of database error.
     */
    public Person find(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE personID = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("personID"),
                                    rs.getString("username"),
                                    rs.getString("firstName"),
                                    rs.getString("lastName"),
                                    rs.getString("gender"),
                                    rs.getString("fatherID"),
                                    rs.getString("motherID"),
                                    rs.getString("spouseID"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to find person in Person table");
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
     * Returns a list of all person objects associated with a given username.
     * @param associatedUsername Username of user whose associated persons we want returned.
     * @return List of persons associated with a user.
     * @throws DataAccessException in case of database error.
     */
    public Person[] findAll(String associatedUsername) throws DataAccessException {
        ArrayList<Person> tempList = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE username = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Person tempPerson = new Person(rs.getString("personID"),
                                               rs.getString("username"),
                                               rs.getString("firstName"),
                                               rs.getString("lastName"),
                                               rs.getString("gender"),
                                               rs.getString("fatherID"),
                                               rs.getString("motherID"),
                                               rs.getString("spouseID"));
                tempList.add(tempPerson);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to find all corresponding persons in Person table");
        } finally {
            if (rs != null) {
                try {
                    rs.close();;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return tempList.toArray(new Person[tempList.size()]);
    }

    /**
     * Removes all persons from database that are associated with the given username.
     * @param associatedUsername user whose persons we are removing from the database.
     */
    public void removeUserPersons(String associatedUsername) throws DataAccessException {
        String sql = "DELETE FROM Person WHERE username = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to remove all corresponding persons in Person table");
        }
    }

    /**
     * Checks the Person table and returns a boolean representing whether a given
     * personID is contained in the table.
     * @param personID String value of personID of person to find
     * @return true if personID is found in database, false otherwise.
     * @throws DataAccessException in case of database error.
     */
    public boolean isPersonFound(String personID) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE personID = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
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
     * Helper function used in a person's family tree generation algorithm.
     * @param personID Person to be linked as a child.
     * @param fatherID Person to be linked as a father.
     * @throws DataAccessException in case of database error.
     */
    public void updateFather(String personID, String fatherID) throws DataAccessException {
        String sql = "UPDATE Person SET fatherID = ? WHERE personID = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fatherID);
            stmt.setString(2, personID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to update father in Person table");
        }
    }

    /**
     * Helper function used in a person's family tree generation algorithm.
     * @param personID Person to be linked as a child.
     * @param motherID Person to be linked as a mother.
     * @throws DataAccessException in case of database error.
     */
    public void updateMother(String personID, String motherID) throws DataAccessException {
        String sql = "UPDATE Person SET motherID = ? WHERE personID = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, motherID);
            stmt.setString(2, personID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Database Error: failed to update mother in Person table");
        }
    }

    /**
     * Helper function that creates parents for a given person, and can do so
     * recursively in order to quickly fill a family tree.
     * @param eventDao Dao used to write to Event table for births, deaths, and marriages.
     * @param username All generated persons will be associated with this username.
     * @param personID ID of person for whom we are generating parents.
     * @param surname Surname of person for whom we are generating parents.
     * @param birthYear Birthyear of person for whom we are generating parents.
     * @param numGenerations Number of generations for which we recursively build parents.
     * @throws DataAccessException in case of database error.
     */
    public void makeParents(EventDao eventDao, String username, String personID,
                            String surname, int birthYear, int numGenerations) throws DataAccessException {
        String fatherName = maleNames.get(new Random().nextInt(maleNames.size()));
        String motherName = femaleNames.get(new Random().nextInt(femaleNames.size()));
        String motherSurname = surNames.get(new Random().nextInt(surNames.size()));
        String fatherID = UUID.randomUUID().toString();
        String motherID = UUID.randomUUID().toString();

        Person father = new Person(fatherID, username, fatherName, surname,
                            "m", null, null, motherID);
        Person mother = new Person(motherID, username, motherName, motherSurname,
                            "f", null, null, fatherID);

        updateFather(personID, fatherID);
        updateMother(personID, motherID);
        insert(father);
        insert(mother);

        eventDao.makeBirthday(username, fatherID, (birthYear - 30));
        eventDao.makeBirthday(username, motherID, (birthYear - 25));
        eventDao.makeDeath(username, fatherID, (birthYear + 2));
        eventDao.makeDeath(username, motherID, (birthYear + 3));
        eventDao.makeMarriage(username, fatherID, motherID, birthYear);
        if (numGenerations > 0) {
            makeParents(eventDao, username, fatherID, surname, (birthYear - 30), (numGenerations - 1));
            makeParents(eventDao, username, motherID, motherSurname, (birthYear - 25), (numGenerations - 1));
        }
    }

    /**
     * Helper function that creates a root Person for a new User.
     * @param eventDao Dao used to write to Event table for births, deaths, and marriages.
     * @param curUser User object who will is acting as the root of a family tree.
     * @param personID ID of person for who is acting as the root of a family tree.
     * @param numGenerations Number of generations for which we recursively build parents.
     * @throws DataAccessException in case of database error.
     */
    public void makeRoot(EventDao eventDao, User curUser, String personID, int numGenerations) throws DataAccessException {
        Person tempPerson = new Person(personID, curUser.getUsername(), curUser.getFirstName(),
                                       curUser.getLastName(), curUser.getGender(),
                                null, null, null);
        insert(tempPerson);
        eventDao.makeBirthday(curUser.getUsername(), personID, 1996);
        if (numGenerations > 0) {
            makeParents(eventDao, curUser.getUsername(), personID, curUser.getLastName(), 1996, (numGenerations - 1));
        }
    }
}
