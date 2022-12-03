package result;

import model.Person;

/**
 * Result of a person query meant to return a list of all persons related to
 * a user with a specified authtoken,
 * containing given fields, success flags, and error messages when applicable.
 */
public class AllPersonResult extends ResultManager {
    /**
     * List of all person objects representing family members of the current user.
     */
    private Person[] data;

    /**
     * Constructor used when generating result for a successful transaction.
     * @param data List of all person objects representing family members of the current user.
     */
    public AllPersonResult(Person[] data) {
        this.data = data;
        this.setMessage(null);
        this.setSuccess(true);
    }

    /**
     * Constructor used when generating result for a failed transaction.
     * @param message Message describing an error used in error handling.
     */
    public AllPersonResult(String message) {
        this.data = null;
        this.setMessage(message);
        this.setSuccess(false);
    }

    public AllPersonResult() {}

    public Person[] getData() { return data; }

    public void setData(Person[] data) { this.data = data; }
}
