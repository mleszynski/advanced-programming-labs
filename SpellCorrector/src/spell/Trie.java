package spell;

public class Trie implements ITrie {
    // declare necessary members
    private final Node root;
    private int wordCount;
    private int nodeCount;

    public Trie() {
        this.root = new Node();
        this.wordCount = 0;
        this.nodeCount = 1;
    }

    @Override
    public void add(String word) {
        // IMPORTANT: remember to cast words to lowercase
        word = word.toLowerCase();
        // iterate through each letter in the word
        Node curNode = this.root;
        char letter;
        int index = 0;
        for (int i = 0; i < word.length(); i++) {
            letter = word.charAt(i);
            index = letter - 'a';

            // check to see if we must add a node
            if (curNode.getChildren()[index] == null) {
                curNode.getChildren()[index] = new Node();
                nodeCount++;
            }
            // check to see if we increment value for last letter
            if (i + 1 == word.length()) {
                // increment number of times word was seen
                curNode.getChildren()[index].incrementValue();
                // check to update wordCount
                if (curNode.getChildren()[index].getValue() == 1) this.wordCount++;
            }
            curNode = curNode.getChildren()[index];
        }
    }

    @Override
    public Node find(String word) {
        // IMPORTANT: cast to lower
        word = word.toLowerCase();
        Node curNode = this.root;
        char letter;
        int index = 0;


        // iterate through all letters in word
        for (int i = 0; i < word.length(); i++) {
            letter = word.charAt(i);
            index = letter - 'a';

            Node next = curNode.getChildren()[index];
            // if we can't get to the word, return null
            if (next == null) {
                return null;
            } else {
                curNode = next;
            }
        }
        // check to see if end of string is actually a word
        if (curNode.getValue() > 0) {
            return curNode;
        }
        else {
            return null;
        }
    }

    @Override
    public int getWordCount() {
        return this.wordCount;
    }

    @Override
    public int getNodeCount() {
        return this.nodeCount;
    }

    @Override
    public String toString(){
        // IMPORTANT: review toString() and toStringHelper()
        StringBuilder curWord = new StringBuilder();
        StringBuilder output = new StringBuilder();
        toStringHelper(root, curWord, output);
        return output.toString();
    }

    private void toStringHelper(Node node1, StringBuilder curWord, StringBuilder output){
        // recursion base case
        if (node1.getValue() > 0) {
            // IMPORTANT: remember to append newlines
            // append word value once we find a word in the dictionary
            output.append(curWord.toString());
            output.append("\n");
        }

        // recursive step
        for (int i = 0; i < 26; ++i) {
                Node child = node1.getChildren()[i];
            if (child != null) {
                char childLetter = (char)('a' + i);
                curWord.append(childLetter);
                toStringHelper(child, curWord, output);
                curWord.deleteCharAt(curWord.length() - 1);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        // IMPORTANT: review equals() and equalsHelper()
        // check for null
        if (obj == null) return false;
        // check for itself
        if (obj == this) return true;
        // check for same class
        if (!(obj instanceof Trie)) return false;

        Trie trie2 = (Trie)obj;
        // IMPORTANT: check equality for all instance variables
        // check word count
        if (this.wordCount != trie2.wordCount) return false;
        // check node count
        if (this.nodeCount != trie2.nodeCount) return false;
        // recursively check trees with helper method
        return equalsHelper(this.root, trie2.root);
    }

    private boolean equalsHelper(Node node1, Node node2) {
        // IMPORTANT: KNOW HOW THIS WORKS DARNIT
        // check count
        if (node1.getValue() != node2.getValue()) return false;
        // go through all child nodes
        for (int i = 0; i < 26; i++) {
            Node next1 = node1.getChildren()[i];
            Node next2 = node2.getChildren()[i];

            // check for non-null children in the same indexes
            if (next1 != null && next2 != null) {
                // recurse on the children
                if (!equalsHelper(next1, next2)) {
                    return false;
                }
            }  // if only one is null, return false
            else if (next1 != null || next2 != null) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        // IMPORTANT: review hashcode method algorithm
        for (int i = 0; i < 26; i++) {
            if (this.root.getChildren()[i] != null) {
                return 31*(i+13*this.nodeCount+this.wordCount);
            }
        }
        return 0;
    }
}
