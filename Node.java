import java.util.ArrayList;
import java.util.List;

class Node {
    String value;
    List<Node> children;

    Node(String value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    // Método para adicionar um filho a este nó
    void addChild(Node child) {
        this.children.add(child);
    }
}