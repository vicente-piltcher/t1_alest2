import java.util.ArrayList;
import java.util.List;

public class Node {
    boolean isRight;
    boolean isMiddle;
    boolean isLeft;
    Node father;
    int level;
    int data;
    List<Node> children;

    public Node(int data, Node father, boolean isRight, boolean isLeft, boolean isMiddle, int level) {
        this.isRight = isRight;
        this.isMiddle = isMiddle;
        this.isLeft = isLeft;
        this.father = father;
        this.level = level;
        this.data = data;
        this.children = new ArrayList<>();
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    @Override
    public String toString() {
        return String.valueOf(data);
    }
}