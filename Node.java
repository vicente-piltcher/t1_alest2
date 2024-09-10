import java.util.ArrayList;
import java.util.List;

class Node {
    int data;
    List<Node> children;

    public Node(int data) {
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