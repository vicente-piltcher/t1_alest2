import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tree {
    private String[][] matrix; // Matriz para armazenar os nós
    private int numRows;
    private int numCols;
    private Node root;

    public Tree(String file) {
        buildTreeFromVisualFile(file);
    }

    public void buildTreeFromVisualFile(String fileName) {

        List<String> lines = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
        
            String line;

            // Primeira linha contém o tamanho da matriz
            if ((line = reader.readLine()) != null) {
                String[] dimensions = line.split("\\s+");
                numRows = Integer.parseInt(dimensions[0]);
                numCols = Integer.parseInt(dimensions[1]);
                matrix = new String[numRows][numCols]; // Inicializar a matriz
            }

            // Mapear os nós para as posições na matriz
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (IOException ferr){
            System.err.println(ferr);
            System.out.println("ARQUIVO NAO ENCONTRADO!");
        }

        // Preencher a matriz com base nas posições da linha, interpretando como um grande "plano cartesiano"
        for (int row = 0; row < lines.size(); row++) {
            String currentLine = lines.get(row);

            for (int col = 0; col < currentLine.length() && col < numCols; col++) {
                char character = currentLine.charAt(col);
                String charString = Character.toString(character);
                matrix[row][col] = charString; // Armazena o caractere de conexão ou espaço

            }
        }

        // Cria os nodos a partir da matriz gerada pelo arquivo txt
        buildTreeFromMatrix(this.numRows);
    }
    
    public void imprimeMatriz(){
        for(int i = 0; i < this.matrix.length; ++i){
            for(int j = 0; j < this.matrix[0].length; ++j){

                System.out.print(this.matrix[i][j]);
            }
            System.out.println();
        }
    }


    private void buildTreeFromMatrix(int tam){

        String firstFound = "";

        //Percorre a matriz da última linha e primeira coluna em busca de um elemento
        for(int i = this.matrix.length - 1; i >= 0; --i){

            for(int j = 0; j < this.matrix[0].length; ++j){

                //verificando se não é nulo e verificando se é um inteiro por regex
                if(this.matrix[i][j] != null){

                    firstFound = this.matrix[i][j];

                    if(this.matrix[i][j] == "\\d+"){
                        //cria o nodo root pois é o primeira ocorrencia encontrada
                        //e chama o startWalking com o nodo nao nulo significando que o primeiro elemento encontrado foi um numero
                        Node node = new Node(Integer.parseInt(firstFound), null, false, false, false, i);
                        this.root = node;
                        startWalking(i, j, firstFound, null, node);


                    } else {
                        
                        //inicia o método mais confuso do mundo
                        //inicia com o nodo nulo pois o primeiro elemento nao necessariamente vai ser um numero
                        startWalking(i, j, firstFound, null, null);
                    }
                    
                }

                
            }

        }

    }


    //O intuito principal de usar o parametro pastNode é para, quando achar um elemento, saber qual o ultimo nodo encontrado, pois era seu pai.
    private void startWalking(int i, int j, String found, String pastElement, Node pastNode){

        //pega e armazena o nivel da árvore que sera usado para verificar a integridade dos filhos na arvore formada
        int nivel = i;
        if(found != null){

            //verifica qual caractere foi encontrado para tomar a decisão de onde caminhar para montar a arvore
            switch (found) {
                case "|":
                    //caso o caracter encontrado for '|' o proximo elemento só pode estar no i - 1 e j, pois segue reto para baixo
                    String nextElement = this.matrix[i - 1][j];
                    startWalking(i - 1, j, nextElement, found, pastNode);
                    break;
                case "V":
                    //caso o caracter encontrado for 'V' o proximo elemento pode estar em i - 1 e j + 1 ou j - 1, assim ele chama para os dois lados para criar a arvore
                    String nextRightEle = this.matrix[i - 1][j + 1];
                    String nextLeftEle = this.matrix[i - 1][j - 1];
                    startWalking(i - 1, j + 1, nextRightEle, found, pastNode);
                    startWalking(i - 1, j + 1, nextLeftEle, found, pastNode);
                    break;
                case "/":
                    //caso o caracter encontrado for '/' o proximo elemento só pode estar no i - 1 e j + 1, pois segue para direita
                    String nextEleRight = this.matrix[i - 1][j + 1];

                    //verifica a presença de outro galho sobrepondo a bifurcação, só pode ser o caractere '|' que sobrepoe
                    //precisamos verificar esse unico caso melhor
                    if(nextEleRight == "|"){
                        nextEleRight = this.matrix[i - 2][j + 2];
                        startWalking(i - 2, j + 2, nextEleRight, found, pastNode);
                    } else {
                        startWalking(i - 1, j + 1, nextEleRight, found, pastNode);
                    }
    
                case "\\":
                    //caso o caracter encontrado for '\' o proximo elemento só pode estar no i - 1 e j - 1, pois segue para esquerda
                    String nextEleLeft = this.matrix[i - 1][j - 1];
                    
                    //verifica sobreposição
                    //precisamos verificar esse unico caso melhor '|'
                    if(nextEleLeft == "|"){
                        nextEleLeft = this.matrix[i - 2][j - 2];
                        startWalking(i - 2, j - 2, nextEleLeft, found, pastNode);
                    } else {
                        startWalking(i - 1, j - 1, nextEleLeft, found, pastNode);
                    }
    
                    break;
                case "W":
                    //no caso do W temos a trifurcação nas 3 proximas colunas de W, onde i - 1, j - 1, j + 1 e j
                    String leftNextEle = this.matrix[i - 1][j - 1];
                    String middleNextEle = this.matrix[i - 1][j];
                    String rightNextEle = this.matrix[i - 1][j + 1];
    
                    startWalking(i - 1, j - 1, leftNextEle, found, pastNode);
                    startWalking(i - 1, j, middleNextEle, found, pastNode);
                    startWalking(i - 1, j + 1, rightNextEle, found, pastNode);
                    break;
                case "\\d+":

                    //caso encontrarmos um elemento precisamos saber qual era o seu elemento passado para determinar se ele pertence a esquerda, direita ou meio.
                    int element = Integer.parseInt(found);
                    
                    //switch case para verificar o elemento que levou até o numero.
                    switch (pastElement) {
                        //caso seja '/' o elemento encontrado esta presente a direita da arvore
                        //cria o nodo passando o pastNode e isRight true
                        case "/":
                        Node nodeR = new Node(element, pastNode, true, false, false, nivel);
                        //chama o metodo start walking para continuar andando para direita e passa o nodo criado como ultimo nodo.
                        startWalking(i - 1, j + 1, found, pastElement, nodeR);
                            break;
                        case "\\":
                        //caso seja '\' o elemento encontrado esta presente a esquerda da arvore
                        //cria o nodo passando o pastNode e isLeft true
                        Node nodeL = new Node(element, pastNode, false, true, false, nivel);
                        //chama o metodo starwalking para continuar andando para esquerda
                        startWalking(i - 1, j - 1, found, pastElement, nodeL);
                            break;
                        case "|":
                        //caso seja '|' o elemento encontrado esta presente no meio
                        //cria o nodo passando isMiddle true
                        Node nodeM = new Node(element, pastNode, false, false, true, nivel);
                        //chama o metodo startwalking e continua andando para baixo.
                        startWalking(i - 1, j, found, pastElement, nodeM);
                        
                        //caso seja W o elemento passado precisamos de mais testes para verificar a posição exata do elemento
                        case "W":

                            //verifica em que posição o W está em relação ao elemento encontrado!

                            //caso o 'W' esteja na linha acima (i + 1) e j, significa que nosso elemento está no meio
                            if (this.matrix[i+1][j] == "W"){
                                //cria o nodo e chama o startwalking para continuar andando pelo meio
                                Node nodeMW = new Node(element, pastNode, false, false, true, nivel);
                                startWalking(i - 1, j, found, pastElement, nodeMW);                               
                            } 
                            //caso o 'W' esteja na linha acima (i + 1) e (j + 1), significa que nosso elemento está a esquerda
                            else if(this.matrix[i + 1][j+1] == "W"){
                                //cria o nodo e chama startWalking para continuar andando para esquerda
                                Node nodeLW = new Node(element, pastNode, true, false, false, nivel);
                                startWalking(i - 1, j - 1, found, pastElement, nodeLW);
                            } 
                            //caso o 'W' esteja na linha acima (i + 1) e (j - 1), significa que nosso elemento está a direita                            
                            else if (this.matrix[i+1][j-1] == "W"){
                                //cria o nodo e chama startWalking e continua andando para direita
                                Node nodeRW = new Node(element, pastNode, false, true, false, nivel);
                                startWalking(i - 1, j + 1, found, pastElement, nodeRW);
                            }
                            break;
                        
                        //caso seja V o elemento passado precisamos verificar se nosso elemento esta a esquerda ou direita
                        case "V":
                            //caso esteja na linha acima (i + 1) e (j + 1) nosso elemento esta a esquerda
                            if (this.matrix[i+1][j+1] == "V"){
                                //criamos o nodo e continuamos indo para esquerda
                                Node nodeLV = new Node(element, pastNode, false, true, false, nivel);
                                startWalking(i - 1, j - 1, found, pastElement, nodeLV);
                            } 
                            //caso esteja na linha acima (i + 1) e (j - 1) nosso elemento esta a direita
                            else if (this.matrix[i+1][j-1] == "V"){
                                //cria o nodo e continua indo para direita
                                Node nodeRV = new Node(element, pastNode, true, false, false, nivel);
                                startWalking(i - 1, j + 1, found, pastElement, nodeRV);
                            }
                            break;
                        default:
                            break;
                    }
                    break;
    
                    
                
            }
        } else {
            //esse else é para caso o found seja null, o que não é pra acontecer por causa da verificação que fazemos antes da primeira chamada de startWalking
            startWalking(i - 1, j, found, pastElement, pastNode);
        }
        
        

    }


    /*
     * Esse metodo e o displayNode acho que nao estao funcionando direito pois nao imprimem nada (foi o chatgpt q fez esses)
     */
    public void displayTree(){
        displayNode(root, 0);
    }

    // Método para imprimir a árvore em ordem (pré-ordem)
    private void displayNode(Node node, int level) {
        if (node == null) return;

        // Indentar de acordo com o nível na árvore
        System.out.println("  ".repeat(level) + node.data);

        for (Node child : node.children) {
            displayNode(child, level + 1);
        }
    }
}