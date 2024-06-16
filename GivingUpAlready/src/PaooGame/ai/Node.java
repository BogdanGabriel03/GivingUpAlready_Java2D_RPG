package PaooGame.ai;

public class Node {
    Node parent;
    public int col;
    public int row;
    int gCost;
    int hCost;
    int fCost;
    boolean solid;
    boolean open;
    boolean checked;

    public Node(int row, int col) {
        this.col=col;
        this.row=row;
    }
}
