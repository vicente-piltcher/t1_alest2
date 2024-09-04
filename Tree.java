class Tree {
    Node root;

    Tree(String rootValue) {
        root = new Node(rootValue);
    }

    // Método para adicionar um novo filho a um nó específico
    Node addChild(Node parent, String childValue) {
        Node childNode = new Node(childValue);
        parent.addChild(childNode);
        return childNode;
    }

    // Método para imprimir a árvore em ordem (pré-ordem)
    /*void printTree(Node node, String prefix) {
        if (node != null) {
            System.out.println(prefix + node.value);
            for (Node child : node.children) {
                printTree(child, prefix + "-- ");
            }
        }
    }*/
}