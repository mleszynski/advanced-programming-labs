package encodeDecode;

/**
 * Class which contains a list of names used for person generation.
 */
public class NamesList {
    /**
     * List of possible name Strings for person generation.
     */
    private String[] data;

    public NamesList() { this.data = new String[152]; }

    public String[] getNames() {
        return data;
    }

    public void setNames(String[] names) {
        this.data = names;
    }
}
