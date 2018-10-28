import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Decoder {

    private int alphabetSize;
    private int numberOfStrings;
    private int puzzleSize;

    private char[] alphabet;
    private char[][] puzzle;
    private Map<Integer, List<String>> stringMap;

    private char[][] solution;


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

            //Reading the input stringMap
            stringMap = new HashMap<>();
            for (int i = 0; i < numberOfStrings; i++) {
                String word = reader.readLine();
                if (stringMap.get(word.length()) != null) {
                    stringMap.get(word.length()).add(word);
                } else {
                    stringMap.put(word.length(), new ArrayList<>(Collections.singletonList(word)));
                }
            }

        }catch(Throwable e){
            System.out.println("NO");
        }
        /* LEGACY CODE
        while ((line = reader.readLine()) != null) {
            stringMap.add(line);
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

    public Map<Integer, List<String>> getStringMap() {
        return stringMap;
    }

    public boolean checkCrossword() {
        solution = new char[puzzleSize][puzzleSize];
        Map<Coordinate, Information> wordInfo = new HashMap<>();

        for (int row = 0; row < puzzleSize; row++) {
            int wordLength = 0;
            for (int column = 0; column <= puzzleSize; column++) {
                if (column == puzzleSize || puzzle[row][column] == '#') {
                    if (column != puzzleSize) solution[row][column] = '#';
                    if (wordLength == 0) continue;

                    Coordinate coordinate = new Coordinate(row ,column);
                    Information info = wordInfo.get(coordinate);
                    if (info == null) {
                        info = new Information();
                    }

                    List<String> possibleWords = stringMap.get(wordLength);
                    boolean wordLeft = possibleWords.size() > info.wordsTried;
                    if (wordLeft) {
                        String word = possibleWords.get(info.wordsTried);
                        info.wordsTried++;
                        for (int k = 1; k <= wordLength; k++) {
                            solution[row][column - k] = word.charAt(word.length() - k);
                        }
                    } else {
                        for (int i = 0; i <= puzzleSize; i++) {
                            wordInfo.remove(new Coordinate(row, i));
                        }
                        row -= 2;
                        if (wordInfo.keySet().size() == 0) {
                            return false;
                        }
                        break;
                    }

                    int offendingColumn = checkHorizontal(row + 1,
                            column - 1, wordLength);
                    if (offendingColumn != -1) {
                        wordInfo.put(coordinate, info);
                        column--;
                        continue;
                    }

                    wordInfo.put(coordinate, info);
                    wordLength = 0;
                } else {
                    wordLength++;
                }
            }
        }


        return true;
    }

    private int checkHorizontal(int maxRowToCheck, int maxColumnToCheck, int wordLength) {
        for (int column = maxColumnToCheck; column >= 0 && wordLength != 0 ; column--, wordLength--) {
            if (maxRowToCheck == puzzleSize || puzzle[maxRowToCheck][column] == '#') {
                StringBuilder stringBuilder = new StringBuilder();
                for (int row = maxRowToCheck - 1; row >= 0; row--) {
                    if (solution[row][column] == '#') break;
                    stringBuilder.append(solution[row][column]);
                }
                String wordToCheck = stringBuilder.reverse().toString();
                List<String> strings = stringMap.get(wordToCheck.length());
                if (strings == null || !strings.contains(wordToCheck)) {
                    return column;
                }
            }
        }

        return -1;
    }

    public char[][] getNewCrossword() {
        return solution;
    }

    private class Coordinate {
        int row;
        int column;

        Coordinate(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return row == that.row &&
                    column == that.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }
    }

    private class Information {
        int wordsTried = 0;
    }
}
