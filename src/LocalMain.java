import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Collectors;


public class LocalMain {

    public static void main(String [ ] args)
    {
        String fileName = "2";
//        saveToFile(generateCPC(), fileName);
        runWithFile(fileName);
//        runBenchmark();

    }

    private static void runWithFile(String fileName) {
        String directory = System.getProperty("user.dir") + "/src/AutoGenFiles/";
        String filepath = directory + fileName + ".CPC";

        try{
            final long startTimeD = System.currentTimeMillis();
            Decoder input = new Decoder(new FileReader(filepath));
            final long endTimeD = System.currentTimeMillis();
            System.out.println("Decode: " + (endTimeD  - startTimeD));
            final long startTime = System.currentTimeMillis();
            Solver solver = new Solver(input.getPuzzleSize(), input.getPuzzle(),
                    input.getStrings(), input.getStringSet(), input.getPrefixTrie());
            final long endTime = System.currentTimeMillis();
            System.out.println("Construct Solver: " + (endTime - startTime));
            final long startTimeSolve = System.currentTimeMillis();
            boolean solve = solver.solve();
            final long endTimeSolve = System.currentTimeMillis();
            System.out.println("Solve: " + (endTimeSolve - startTimeSolve));
            if (solve) {
                solver.printSolution();
            }
            else {
                System.out.println("RIP");
                solver.printSolution();
            }

            System.out.println("Total execution time: " + (endTimeSolve - startTimeD));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private static void runBenchmark()  {
        String directory = System.getProperty("user.dir") + "/src/CPCFiles/";
        try {
            for (Path file : Files.walk(Paths.get(directory)).filter(Files::isRegularFile).collect(Collectors.toList())) {
                System.out.println(file);
                try {
                    FileReader fileReader = new FileReader(file.toString());
                    final long decoderStartTime = System.currentTimeMillis();
                    Decoder input = new Decoder(fileReader);
                    final long decoderEndTime = System.currentTimeMillis();
                    final long solverStartTime = System.currentTimeMillis();
                    Solver solver = new Solver(input.getPuzzleSize(), input.getPuzzle(),
                            input.getStrings(), input.getStringSet(), input.getPrefixTrie());
                    System.out.println("Solved: " + solver.solve());
                    final long solverEndTime = System.currentTimeMillis();
                    System.out.println("Decoder execution time: " + (decoderEndTime - decoderStartTime));
                    System.out.println("Solver execution time: " + (solverEndTime - solverStartTime) + "\n");
                } catch (Exception e) {
                    System.out.println("NO");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveToFile(String CPC, String fileName) {
        String directory = System.getProperty("user.dir") + "/src/AutoGenFiles/" + fileName + ".CPC";
        Path path = Paths.get(directory);
        byte[] bytes = CPC.getBytes();
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateCPC() {
        String entireAlphabet = "a;b;c;d;e;f;g;h;i;j;k;l;m;n;o;p;q;r;s;t;u;v;w;x;y;z";
        String entireAlphabetWithout = "abcdefghijklmnopqrstuvwxyz";

        int alphabetSize = 2;
        int numberOfStrings = 500;
        int sizeOfMatrix = 10;

        double chanceOfPound = 10;

        String alphabet = entireAlphabet.substring(0, alphabetSize * 2 - 1);

        Random rand = new Random();

        String[] matrixLines = new String[sizeOfMatrix];
        for (int i = 0; i < sizeOfMatrix; i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < sizeOfMatrix; j++) {
                line.append((rand.nextInt(101) + 1) > chanceOfPound ? "_" : "#");
                if (j != sizeOfMatrix - 1) line.append(";");
            }
            matrixLines[i] = line.toString();
        }

        String[] lines = new String[numberOfStrings];
        for (int i = 0; i < numberOfStrings; i++) {
            lines[i] = drawFromAlphabet(entireAlphabetWithout.substring(0, alphabetSize),
                    rand.nextInt((sizeOfMatrix - 1) + 1) + 1);
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(alphabetSize).append(";").append(numberOfStrings).append(";").append(sizeOfMatrix).append(";").append("\n");
        stringBuilder.append(alphabet).append("\n");
        for (String line : matrixLines) stringBuilder.append(line).append("\n");
        for (String line : lines) stringBuilder.append(line).append("\n");
        return stringBuilder.toString();
    }

    private static String drawFromAlphabet(String alphabet, int length) {
        int max = alphabet.length() - 1;
        int min = 0;
        Random rand = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(alphabet.charAt(rand.nextInt((max - min) + 1) + min));
        }
        return stringBuilder.toString();
    }

}
