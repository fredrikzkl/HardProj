import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Decoder {

    private int alphabetSize;
    private int numberOfStrings;
    private int puzzleSize;

    private char[] alphabet;
    private char[][] puzzle;
    public List<String> strings;


    public Decoder(FileReader file) throws IOException{
        try {
            BufferedReader reader = new BufferedReader(file);
            readAttributes(reader.readLine());
            readAlphabet(reader.readLine());

            //Reading the matrix bruh
            puzzle = new char[puzzleSize][puzzleSize];
            for (int i = 0; i < puzzleSize; i++) {
                readMatrixRow(reader.readLine(), i);
            }

            //Reading the input strings
            strings = new ArrayList<>();
            for (int i = 0; i < numberOfStrings; i++) {
                strings.add(reader.readLine());
            }

        }catch(Throwable e){
            System.out.println("NO");
        }
        /* LEGACY CODE
        while ((line = reader.readLine()) != null) {
            strings.add(line);
        }*/
        System.out.println("YES");
    }


    private void readMatrixRow(String s, int row){
        String[] col = s.split(";");
        for(int i = 0 ; i < puzzleSize; i++){
            puzzle[row][i] = col[i].charAt(0);
        }
    }

    private void readAlphabet(String s){
        String[] temp = s.split(";");
        alphabet = new char[alphabetSize];

        for(int i = 0 ; i < alphabetSize; i++){
            alphabet[i] = temp[i].charAt(0);
        }
    }

    private void readAttributes(String s){
        String[] temp = s.split(";");
        alphabetSize = Integer.parseInt(temp[0]);
        numberOfStrings = Integer.parseInt(temp[1]);
        puzzleSize = Integer.parseInt(temp[2]);
    }

    public char[] getAlphabet() {
        return alphabet;
    }

    public char[][] getPuzzle() {
        return puzzle;
    }

    public List<String> getStrings() {
        return strings;
    }
}
