import java.util.*;
import java.util.stream.Collectors;

class Solver {
    private int puzzleSize;
    private char[][] puzzle;
    
    private List<String> strings;
    private Map<Integer, List<String>> stringMap;

    private char[][] solution;
    private long loops = 0;

    Solver(int puzzleSize, char[][] puzzle, List<String> strings) {
        this.puzzleSize = puzzleSize;
        this.puzzle = puzzle;
        this.strings = strings;
        this.stringMap = strings.stream().collect(Collectors.groupingBy(String::length));
        this.solution = copyPuzzle(this.puzzle);
    }

    private char[][] copyPuzzle(char[][] puzzle) {
        char[][] result = new char[puzzleSize][puzzleSize];
        for (int row = 0; row < puzzleSize; row++) {
            for (int column = 0; column < puzzleSize; column++) {
                if (puzzle[row][column] == '#') result[row][column] = '#';
            }
        }
        return result;
    }

    boolean solve() {
        Map<Coordinate, Information> wordInfo = new HashMap<>();
        Stack<Coordinate> words = new Stack<>();
        Coordinate currentWord = null;
        loops = 0;
        for (int row = 0; row < puzzleSize; row++) {
            int wordLength = 0;
            for (int column = 0; column <= puzzleSize; column++) {
                if (column == puzzleSize || puzzle[row][column] == '#') {
                    loops++;
                    if (wordLength == 0) continue;

                    Coordinate coordinate = new Coordinate(row ,column);
                    if (currentWord == null || !currentWord.equals(coordinate)) {
                        if (currentWord != null) words.push(currentWord);
                        currentWord = coordinate;
                    }
                    
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
                        String temp = getSolutionAsString();
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
            StringBuilder stringBuilder = new StringBuilder();
            for (int row = maxRowToCheck - 1; row >= 0; row--) {
                if (solution[row][column] == '#') break;
                stringBuilder.append(solution[row][column]);
            }
            String wordToCheck = stringBuilder.reverse().toString();
            if (maxRowToCheck == puzzleSize || puzzle[maxRowToCheck][column] == '#') {
                List<String> strings = stringMap.get(wordToCheck.length());
                if (strings == null || !strings.contains(wordToCheck)) {
                    return column;
                }
            } else if (strings.stream().noneMatch(s -> s.startsWith(wordToCheck))) {
                return column;
            }
        }

        return -1;
    }

    char[][] getSolution() {
        return solution;
    }

    long getLoops() {
        return loops;
    }

    String getSolutionAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (char[] row : solution) {
            for (int j = 0; j < solution.length; j++) {
                stringBuilder.append(row[j] != 0 ? row[j] : '_');
                if (j != solution.length - 1) stringBuilder.append(";");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
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