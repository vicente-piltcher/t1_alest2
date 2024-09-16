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

    public Tree(String file) {
        vet = new ArrayList<>();
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

                /*if(charString.trim().isEmpty()){
                    charString = "@";
                }*/

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
        boolean foundFirstFound = false;

        //Percorre a matriz da última linha e primeira coluna em busca de um elemento
        for(int i = this.matrix.length - 1; i >= 0; --i){

            for(int j = 0; j < this.matrix[0].length; ++j){
                

                //verificando se não é nulo e verificando se é um inteiro por regex
                if(this.matrix[i][j].trim().isEmpty() == false && foundFirstFound == false){

                    firstFound = this.matrix[i][j];
                    foundFirstFound = true;

                    System.out.print(i + " ");
                    System.out.println(j + " ");
                    System.out.println(":"+firstFound);

                    //inicia o método mais confuso do mundo
                    //inicia com o nodo nulo pois o primeiro elemento nao necessariamente vai ser um numero
                    initWalking(i, j, firstFound, 0);
                    
                }
                
            }

        }

        int maiorSoma = Collections.max(vet);

        System.out.println("A maior soma parcial é: " + maiorSoma);

    }

    public void initWalking(int row, int col, String found, int somaParcial){

        boolean fn = found.matches("\\d+");
        if(fn == true){
            System.out.println("@@@@ FOUND A NUMBER");            
            found = "d";
        }


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
                System.out.println(" ********* FIM DO GALHO ********* ");
                System.out.println("SOMA PARCIAL: " + somaParcial);
                vet.add(somaParcial);
                break;
        
            default:
                break;
        }

    }

    public void startWalkingForward(int row, int col, int somaParcial){

        System.out.println("---------- Indo para o Meio ----------");

        String element = this.matrix[row][col];

        while (!element.equals("V") && !element.equals("W") && !element.equals("#")){

            //System.out.println("ROW: "+row);

            boolean fn = element.matches("\\d+");
            if(fn == true){
                System.out.println("@@@@ FOUND A NUMBER: " + element);            
                int valElement = Integer.parseInt(element);
                somaParcial += valElement;
            }

            element = this.matrix[row -= 1][col];
        } 

        System.out.println("somaParcial: " + somaParcial);

        initWalking(row, col, element, somaParcial);

    }

    public void startWalkingLeft(int row, int col, int somaParcial){
        String element = this.matrix[row][col];

        System.out.println("---------- Indo para Esquerda ----------");

        while (!element.equals("V") && !element.equals("W") && !element.equals("#")){

            //System.out.println("ROW: "+row + " COL: " + col);

            boolean fn = element.matches("\\d+");
            if(fn == true){
                System.out.println("@@@@ FOUND A NUMBER: " + element);            
                int valElement = Integer.parseInt(element);
                somaParcial += valElement;
            }

            element = this.matrix[row -= 1][col -= 1];
        } 

        System.out.println("somaParcial: " + somaParcial);

        initWalking(row, col, element, somaParcial);
    }

    public void startWalkingRight(int row, int col, int somaParcial){
        String element = this.matrix[row][col];

        System.out.println("---------- Indo para Direita ----------");

        while (!element.equals("V") && !element.equals("W") && !element.equals("#")){

            //System.out.println("ROW: "+row + " COL: " + col);

            boolean fn = element.matches("\\d+");
            if(fn == true){
                System.out.println("@@@@ FOUND A NUMBER: " + element);            
                int valElement = Integer.parseInt(element);
                somaParcial += valElement;
            }

            element = this.matrix[row -= 1][col += 1];
        } 

        System.out.println("somaParcial: " + somaParcial);

        initWalking(row, col, element, somaParcial);
    }
}