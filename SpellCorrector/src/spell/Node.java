package spell;

public class Node implements INode {
    // declare necessary members
    private int value;
    private Node[] children;

    public Node() {
        this.value = 0;
        this.children = new Node[26];
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public void incrementValue() {
        this.value++;
    }

    @Override
    public Node[] getChildren() {
        return this.children;
    }
}
