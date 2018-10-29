import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

class Decoder {
    private int alphabetSize;
    private int numberOfStrings;
    private int puzzleSize;

    private Set<Character> alphabet;
    private char[][] puzzle;
    private String[][] strings;
    private LinkedHashSet<String> stringSet;
    private TrieNode prefixTrie;

    Decoder(InputStreamReader file) throws Exception{
        BufferedReader reader = new BufferedReader(file);

        readAttributes(reader.readLine());
        readAlphabet(reader.readLine());
        readMatrix(reader);
        readStrings(reader);
    }

    private void readStrings(BufferedReader reader) throws IOException {
        stringSet = new LinkedHashSet<>();
        strings = new String[puzzleSize][numberOfStrings];
        prefixTrie = new TrieNode(new LinkedHashMap<>());
        for (int i = 0; i < numberOfStrings; i++) {
            String word = reader.readLine();
            if (word == null) throw new IllegalArgumentException("Wrong number of strings");
            if (!checkStringInAlphabet(word)) throw new IllegalArgumentException("String not in alphabet");
            if (stringSet.add(word)) {
                int index = word.length() - 1;
                for (int j = 0; j < strings[index].length; j++) {
                    if (strings[index][j] == null) {
                        strings[index][j] = word;
                        break;
                    }
                }
                prefixTrie.insertString(word);
            }
        }

        Integer[] sizes = new Integer[puzzleSize];
        for (int i = 0; i < puzzleSize; i++) {
            int size = 0;
            for (String string : strings[i]) {
                if (string == null) break;
                size++;
            }
            sizes[i] = size;
        }

        for (int i = 0; i < puzzleSize; i++) {
            String[] newArray = new String[sizes[i]];
            System.arraycopy(strings[i], 0, newArray, 0, sizes[i]);
            strings[i] = newArray;
        }
        if (reader.readLine() != null) throw new IllegalArgumentException("Wrong number of strings");
    }

    private boolean checkStringInAlphabet(String word) {
        for (int i = 0; i < word.length(); i++) {
            char a = word.charAt(i);
            if (!alphabet.contains(a)) return false;
        }
        return true;
    }

    private void readMatrix(BufferedReader reader) throws IOException {
        puzzle = new char[puzzleSize][puzzleSize];
        puzzle = new char[puzzleSize][puzzleSize];
        for (int i = 0; i < puzzleSize; i++) {
            readMatrixRow(reader.readLine(), i);
        }
    }

    private void readMatrixRow(String s, int row) {
        String[] col = s.split(";");
        if (col.length != puzzleSize) throw new IllegalArgumentException("Matrix row too long");
        for(int i = 0 ; i < puzzleSize; i++){
            char symbol = col[i].charAt(0);
            if ((symbol != '_' && symbol != '#')) throw new IllegalArgumentException("Matrix containing wrong symbol");
            puzzle[row][i] = col[i].charAt(0);
        }
    }

    private void readAlphabet(String s){
        String[] temp = s.split(";");
        if (temp.length != alphabetSize) throw new IllegalArgumentException("Wrong length of alphabet");
        alphabet = new HashSet<>();

        for(int i = 0 ; i < alphabetSize; i++){
            if (!alphabet.add(temp[i].charAt(0))) throw new IllegalArgumentException("Alphabet not a set");
        }
    }

    private void readAttributes(String s){
        String[] temp = s.split(";");
        if (temp.length != 3) throw  new IllegalArgumentException("Wrong number of attributes");
        alphabetSize = Integer.parseInt(temp[0]);
        numberOfStrings = Integer.parseInt(temp[1]);
        puzzleSize = Integer.parseInt(temp[2]);
    }

    int getPuzzleSize() {
        return puzzleSize;
    }

    char[][] getPuzzle() {
        return puzzle;
    }

    String[][] getStrings() {
        return strings;
    }

    TrieNode getPrefixTrie() {
        return prefixTrie;
    }

    LinkedHashSet<String> getStringSet() {
        return stringSet;
    }
}
