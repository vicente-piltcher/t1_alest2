import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Tree {
    private String[][] matrix; // Matriz para armazenar os nós
    private int numRows;
    private int numCols;
    private List<Integer> vet;

    private long initialTime;

    public Tree(String file) {
        vet = new ArrayList<>();
        buildTreeFromVisualFile(file);
    }


    //Metodo que recebe o arquivo que iremos trabalhar
    public void buildTreeFromVisualFile(String fileName) {

        //inicia o tempo de processamento
        this.initialTime = System.currentTimeMillis();

        List<String> lines = new ArrayList<>();



        //Logica que le o arquivo e insere cada linha em um array de linhas
        //também ja inicializa a matriz pegando os parametros da primeira linha que informa o tamanho da matriz
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

        // Preencher a matriz com base no charAt do vetor de linhas, interpretando como um grande "plano cartesiano"
        for (int row = 0; row < lines.size(); row++) {
            String currentLine = lines.get(row);

            for (int col = 0; col < currentLine.length() && col < numCols; col++) {
                char character = currentLine.charAt(col);
                String charString = Character.toString(character);

                matrix[row][col] = charString; // Armazena o caractere de conexão ou espaço

            }
        }

        // Chama o método que começa a percorrer a matriz
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


    //metodo que vai interpretar o primeiro caractere da matriz para chamar a recursão
    private void buildTreeFromMatrix(int tam){

        String firstFound = "";
        boolean foundFirstFound = false;

        //Percorre a matriz da última linha e primeira coluna em busca de um elemento
        for(int i = this.matrix.length - 1; i >= 0; --i){

            for(int j = 0; j < this.matrix[0].length; ++j){
                

                //verificando se não é nulo e verifica se ja foi encontrado algum outro elemento
                //como o initWalking nesse bloco sera chamado uma vez, esses dois 'for' vao percorrer toda matriz até acabar
                // e depois só vao imprimir a maior soma ao sair dos 2 'for'
                if(this.matrix[i][j].trim().isEmpty() == false && foundFirstFound == false){

                    firstFound = this.matrix[i][j];
                    foundFirstFound = true;


                    //inicia a caminhada recursiva com o primeiro elemento encontrado na arvore
                    //a arvore é percorrida da matriz na ultima linha e primeira coluna, ou seja vai pegar realmente o inicio da arvore
                    initWalking(i, j, firstFound, 0);
                    
                }
                
            }

        }

        //retira o maior inteiro presente em vet (vet é o vetor de somas parciais de cada galho)
        int maiorSoma = Collections.max(vet);

        System.out.println("A maior soma parcial é: " + maiorSoma + "\n");
        System.out.printf("O tempo de execução foi: %.4f ms%n", (System.currentTimeMillis() - this.initialTime) / 1000d);

    }


    //metodo recursivo que sempre passa row e col para identificar a posição que estamos na matriz
    //o found ele serve para identificarmos para onde devemos ir quando esse método é chamado
    //a soma parcial é o inteiro que retiramos de cada galho
    public void initWalking(int row, int col, String found, int somaParcial){

        //switch case no found para chamarmos os métodos que caminham sem parar para sua direção até encontrar uma
        //ocorrencia de bifurcação, trifurcação ou uma folha
        switch (found) {
            case "|":
                startWalkingForward(row - 1, col, somaParcial);
                break;

            case "W":
                startWalkingForward(row - 1, col, somaParcial);
                startWalkingLeft(row - 1, col - 1, somaParcial);
                startWalkingRight(row - 1, col + 1, somaParcial);
                
                break;
            case "V":
                startWalkingLeft(row - 1, col - 1, somaParcial);
                startWalkingRight(row - 1, col + 1, somaParcial);
                break;
            case "#":
                // quando achamos folha adicionamos a soma parcial do galho ao vetor
                vet.add(somaParcial);
                break;
        
            default:
                break;
        }

    }

    //metodo que caminha para frente até encontrar uma ocorrencia de V, W ou # na direção que ele veio (que no caso é para frente --> '|')
    public void startWalkingForward(int row, int col, int somaParcial){

        //System.out.println("---------- Indo para o Meio ----------");

        String element = this.matrix[row][col];

        //caminha para frente independente de onde estamos até encontrar uma das ocorrenciais
        while (!element.equals("V") && !element.equals("W") && !element.equals("#")){

            //verifica se estamos passando por um inteiro
            //s e sim transformamos a string em inteiro e adicionamos a somaParcial
            boolean fn = element.matches("\\d+");
            if(fn == true){     
                int valElement = Integer.parseInt(element);
                somaParcial += valElement;
            }

            //incrementamos o element descendo para baixo (caso de walkForward --> '|')
            element = this.matrix[row -= 1][col];
        } 

        //caso acharmos uma daquelas ocorrenciais chamamos o initWalking novamente para caminhar para um dos caminhos que achamos
        initWalking(row, col, element, somaParcial);

    }

    //metodo que caminha para esquerda até encontrar uma ocorrencia de V, W ou # na direção que ele veio (que no caso é para esquerda --> '\')
    public void startWalkingLeft(int row, int col, int somaParcial){
        String element = this.matrix[row][col];

        //enquanto não encontrarmos essas ocorrencias continua seguindo para esquerda capturando todos elementos encontrados
        while (!element.equals("V") && !element.equals("W") && !element.equals("#")){

            //verifica inteiro e soma com a somaParcial
            boolean fn = element.matches("\\d+");
            if(fn == true){
                //System.out.println("@@@@ FOUND A NUMBER: " + element);            
                int valElement = Integer.parseInt(element);
                somaParcial += valElement;
            }

            //itera em element para continuar indo para esquerda
            element = this.matrix[row -= 1][col -= 1];
        } 

        //ao achar outra ocorrencia chama initWalking para continuar o caminhamento
        initWalking(row, col, element, somaParcial);
    }

    //metodo que caminha para direita até encontrar uma ocorrencia de V, W ou # na direção que ele veio (que no caso é para direita --> '/')
    public void startWalkingRight(int row, int col, int somaParcial){
        String element = this.matrix[row][col];


        //continua caminhando até encontrar uma das ocorrências
        while (!element.equals("V") && !element.equals("W") && !element.equals("#")){

            //verifica se passamos por um elemento
            boolean fn = element.matches("\\d+");
            if(fn == true){        
                int valElement = Integer.parseInt(element);
                somaParcial += valElement;
            }

            element = this.matrix[row -= 1][col += 1];
        }

        //chama o metodo init walking para continuarmos a recursão
        initWalking(row, col, element, somaParcial);
    }
}