import java.util.*;

class Solver {
    private int puzzleSize;
    private char[][] puzzle;
    
    private String[][] strings;
    private LinkedHashSet<String> stringSet;
    private TrieNode prefixTrie;
    private Word rootWord = null;

    private char[][] solution;

    Solver(Decoder decoder) {
        this.puzzleSize = decoder.getPuzzleSize();
        this.puzzle = decoder.getPuzzle();
        this.strings = decoder.getStrings();
        this.stringSet = decoder.getStringSet();
        this.prefixTrie = decoder.getPrefixTrie();
        initiateSolution();
    }

    private void initiateSolution() {
        this.solution = new char[puzzleSize][puzzleSize];
        Word lastWord = null;
        for (int row = 0; row < puzzleSize; row++) {
            int wordLength = 0;
            for (int column = 0; column < puzzleSize; column++) {
                if (puzzle[row][column] == '#') {
                    solution[row][column] = '#';
                    if (wordLength != 0) {
                        lastWord = addWord(new Word(row, column, wordLength), lastWord);
                        wordLength = 0;
                    }
                } else {
                    wordLength++;
                    if (column == puzzleSize - 1)
                        lastWord = addWord(new Word(row, column + 1, wordLength), lastWord);
                }
            }
        }
    }

    private Word addWord(Word newWord, Word lastWord) {
        if (rootWord == null) {
            rootWord = newWord;
            return rootWord;
        } else {
            lastWord.child = newWord;
            return newWord;
        }

    }

    boolean solve() {
        return solveRecHelper(rootWord);
    }

    private boolean solveRecHelper(Word word) {
        String[] possibleWords = strings[word.length - 1];
        boolean horizontalCheck = false;
        int wordsTried = 0;
        while (!horizontalCheck && possibleWords.length > wordsTried) {
            String newWord = possibleWords[wordsTried++];
            fillSolution(newWord, word);
            horizontalCheck = checkHorizontal(word.row + 1,word.column - 1, word.length);
            horizontalCheck = horizontalCheck && (word.child == null || solveRecHelper(word.child));
        }
        return horizontalCheck;
    }

    private void fillSolution(String newWord, Word word) {
        for (int k = 1; k <= word.length; k++) {
            solution[word.row][word.column - k] = newWord.charAt(word.length - k);
        }
    }

    private boolean checkHorizontal(int maxRowToCheck, int maxColumnToCheck, int wordLength) {
        for (int column = maxColumnToCheck; column >= 0; column--) {
            String wordToCheck = buildWord(maxRowToCheck - 1, column);
            if (maxRowToCheck == puzzleSize || solution[maxRowToCheck][column] == '#') {
                if (!doesStringsContainWord(wordToCheck)) return false;
            } else {
                if (!doesStringsContainWordsStartingWith(wordToCheck)) return false;
            }
        }
        return true;
    }

    private String buildWord(int maxRow, int column) {
        StringBuilder stringBuilder = new StringBuilder(puzzleSize);
        for (int row = maxRow; row >= 0; row--) {
            char c = solution[row][column];
            if (c == '#') break;
            stringBuilder.append(c);
        }
        return stringBuilder.reverse().toString();
    }

    private boolean doesStringsContainWord(String wordToCheck) {
        return stringSet.contains(wordToCheck);
    }

    private boolean doesStringsContainWordsStartingWith(String wordToCheck) {
        return this.prefixTrie.prefix(wordToCheck);
    }

    void printSolution() {
        StringBuilder stringBuilder = new StringBuilder(puzzleSize * 2);
        for (int row = 0; row < puzzleSize; row++) {
            for (int column = 0; column < puzzleSize; column++) {
                stringBuilder.append(solution[row][column]);
                if (column != puzzleSize - 1) stringBuilder.append(";");
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
        Word child;

        Word(int row, int column, int length) {
            this.row = row;
            this.column = column;
            this.length = length;
            this.wordsTried = 0;
        }
    }
}