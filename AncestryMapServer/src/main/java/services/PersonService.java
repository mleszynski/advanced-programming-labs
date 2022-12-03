package services;

import dao.*;
import model.*;
import result.PersonResult;

/**
 * Handles person request by passing it off to the appropriate Daos.
 */
public class PersonService {
    private Database db;

    public PersonService() {
        this.db = new Database();
    }

    /**
     * Returns the single Person object with the specified ID (if the person
     * is associated with the current user). The current user is determined
     * by the provided authtoken.
     * @param personID ID of desired Person object.
     * @param authtoken Token used to identify current user.
     * @return PersonResult object with information or error message.
     */
    public PersonResult onePerson(String personID, String authtoken) {
        PersonResult result = new PersonResult();

        try {
            db.openConnection();

            if (db.getAuthTokenDao().isTokenFound(authtoken)) {
                String username = db.getAuthTokenDao().find(authtoken).getUsername();

                if (db.getPersonDao().isPersonFound(personID)) {
                    Person tempPerson = db.getPersonDao().find(personID);
                    String tempUsername = tempPerson.getAssociatedUsername();

                    if(username.equals(tempUsername)) {
                        result.setAssociatedUsername(tempPerson.getAssociatedUsername());
                        result.setPersonID(tempPerson.getPersonID());
                        result.setFirstName(tempPerson.getFirstName());
                        result.setLastName(tempPerson.getLastName());
                        result.setGender(tempPerson.getGender());
                        if (tempPerson.getFatherID() != null) {
                            result.setFatherID(tempPerson.getFatherID());
                        }
                        if (tempPerson.getMotherID() != null) {
                            result.setMotherID(tempPerson.getMotherID());
                        }
                        if (tempPerson.getSpouseID() != null) {
                            result.setSpouseID(tempPerson.getSpouseID());
                        }
                        result.setSuccess(true);
                        db.closeConnection(true);
                    } else {
                        result.setMessage("Error: Requested Person does not belong to user");
                        result.setSuccess(false);
                        db.closeConnection(false);
                    }
                } else {
                    result.setMessage("Error: No such personID found");
                    result.setSuccess(false);
                    db.closeConnection(false);
                }
            } else {
                result.setMessage("Error: No such authtoken found");
                result.setSuccess(false);
                db.closeConnection(false);
            }
        } catch (DataAccessException e) {
            result.setMessage("Error: Data access error in PersonService");
            result.setSuccess(false);

            try {
                db.closeConnection(false);
            } catch (DataAccessException d) {
                d.printStackTrace();
            }
        }
        return result;
    }
}
