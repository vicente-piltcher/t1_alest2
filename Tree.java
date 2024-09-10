import java.util.ArrayList;
import java.util.List;

class Tree {
    private Node root;
    private String[] parts;
    private List<Node> nodes;

    public Tree(ArrayList<String> ar) {
        buildTree(ar);
    }

    //Constrói a arvore
    public void buildTree(ArrayList<String> ar){


        //Le o array ao contrário
        for(int i = ar.size(); i < 0; --i){

            //o metodo vai manipular essa linha por todo o código até obter o nodo
            String line = ar.get(i);

            //verifica se tem um ou mais inteiros na linha
            if(line.matches(".*\\d+.*")) {
                //separa por espaços
                parts = line.split("\\s+");
                //para cada parte verifica a presença do inteiro, cria o nodo
                for (String part : parts){
                    if (part.matches("\\d+")) {
                        
                        //converte o valor da string part para inteiro
                        int nodeValue = Integer.parseInt(part);
                        Node node = new Node(nodeValue);
                        nodes.add(node);

                        //pega o primeiro valor para ser raiz (como estamos lendo de cabeça pra baixo funciona(a principio kkkk))
                        if(this.root == null){
                            this.root = node;
                        }
                    }
                }
            }

            if(i + 1 < ar.size()){
                String nextLine = ar.get(i + 1).trim();

                if(nextLine.contains("V") || nextLine.contains("W")){
                    //conectaNodo()
                }
            }
        }

    }

    //nao terminuei 
    private void conectaNodo(List<Node> fathers, String child, boolean isTripleCon){
        String[] childPart = child.split("\\s+");

        for(int i = 0; i < fathers.size(); ++i ) {
            Node father = fathers.get(i);

            int maxChilds = isTripleCon ? 3 : 2;
            int numChilds = 0;

            for(int j = i - 1; j <= i + 1 && numChilds < maxChilds; ++j){

            }
        }


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