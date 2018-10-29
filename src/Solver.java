import java.util.*;

class Solver {
    private int puzzleSize;
    private char[][] puzzle;
    
    private String[][] strings;
    private LinkedHashSet<String> stringSet;
    private TrieNode prefixTrie;
    private List<Word> words;

    private char[][] solution;

    Solver(int puzzleSize, char[][] puzzle, String[][] strings,
           LinkedHashSet<String> stringSet, TrieNode prefixTrie) {
        this.puzzleSize = puzzleSize;
        this.puzzle = puzzle;
        this.strings = strings;
        this.stringSet = stringSet;
        this.prefixTrie = prefixTrie;
        initiateSolution();
    }

    private void initiateSolution() {
        this.solution = new char[puzzleSize][puzzleSize];
        this.words = new ArrayList<>();
        for (int row = 0; row < puzzleSize; row++) {
            int wordLength = 0;
            for (int column = 0; column < puzzleSize; column++) {
                if (puzzle[row][column] == '#') {
                    this.solution[row][column] = '#';
                    if (wordLength != 0) {
                        this.words.add(new Word(row, column, wordLength));
                        wordLength = 0;
                    }
                } else {
                    wordLength++;
                    if (column == puzzleSize - 1) {
                        this.words.add(new Word(row, column + 1, wordLength));
                    }
                }
            }
        }
    }

    boolean solve() {
        for (int currentWord = 0; currentWord < this.words.size(); currentWord++) {
            Word word = this.words.get(currentWord);
            String[] possibleWords = strings[word.length - 1];
            if (possibleWords.length > word.wordsTried) {
                String newWord = possibleWords[word.wordsTried];
                char[] chars = newWord.toCharArray();
                int wordLength = word.length;
                word.wordsTried++;
                for (int k = 1; k <= word.length; k++) {
                    solution[word.row][word.column - k] = chars[wordLength - k];
                }
            } else {
                word.wordsTried = 0;
                currentWord -=2;
                if (currentWord == -2) return false;
                continue;
            }

            if (!checkHorizontal(word.row + 1,word.column - 1, word.length))
                currentWord--;
        }

        return true;
    }

    private boolean checkHorizontal(int maxRowToCheck, int maxColumnToCheck, int wordLength) {
        for (int column = maxColumnToCheck; column >= 0 &&
                maxColumnToCheck - column != wordLength; column--) {
            String wordToCheck = buildWord(maxRowToCheck - 1, column);
            if (maxRowToCheck == puzzleSize || puzzle[maxRowToCheck][column] == '#') {
                if (!doesStringsContainWord(wordToCheck)) return false;
            } else {
                if (!doesStringsContainWordsStartingWith(wordToCheck)) return false;
            }
        }

        return true;
    }

    private String buildWord(int maxRow, int column) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int row = maxRow; row >= 0; row--) {
            if (solution[row][column] == '#') break;
            stringBuilder.append(solution[row][column]);
        }
        return stringBuilder.reverse().toString();
    }

    private boolean doesStringsContainWord(String wordToCheck) {
        return stringSet.contains(wordToCheck);
    }

    private boolean doesStringsContainWordsStartingWith(String wordToCheck) {
        return this.prefixTrie.prefix(wordToCheck);
    }

    char[][] getSolution() {
        return solution;
    }

    void printSolution() {
        StringBuilder stringBuilder = new StringBuilder();
        for (char[] row : solution) {
            for (int j = 0; j < solution.length; j++) {
                stringBuilder.append(row[j]);
                if (j != solution.length - 1) stringBuilder.append(";");
            }
            stringBuilder.append("\n");
        }
        System.out.print(stringBuilder.toString());
    }

    private class Word {
        int row;
        int column;
        int length;
        int wordsTried;

        Word(int row, int column, int length) {
            this.row = row;
            this.column = column;
            this.length = length;
            this.wordsTried = 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Word word = (Word) o;
            return row == word.row &&
                    column == word.column &&
                    length == word.length;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column, length);
        }
    }
}