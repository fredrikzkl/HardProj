import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;


public class Main {

    public static void main(String [ ] args)
    {
        runWithFile();
//        runBenchmark();
//        runForCodeJudge();
    }

    private static void runWithFile() {
        String directory = System.getProperty("user.dir") + "/src/CPCFiles/";
        String filepath = directory + "walls.CPC";

        try{
            Decoder input = new Decoder(new FileReader(filepath));
            Helper.printMatrix(input.getPuzzle());
            System.out.println();
            final long startTime = System.currentTimeMillis();
            Solver solver = new Solver(input.getPuzzleSize(), input.getPuzzle(), input.getStrings());
            if (solver.solve()) {
                Helper.printMatrix(solver.getSolution());
            }
            else {
                System.out.println("RIP");
                System.out.println(solver.getSolutionAsString());
            }
            final long endTime = System.currentTimeMillis();
            System.out.println("Loops: " + solver.getLoops());
            System.out.println("Total execution time: " + (endTime - startTime) + "\n");
        }catch (Exception e){
            System.out.println("File with path: '" + filepath  + "' could not be found.\n\n" + e);
        }
    }

    private static void runForCodeJudge() {
        try{
            Decoder input = new Decoder(new InputStreamReader(System.in));
            Solver solver = new Solver(input.getPuzzleSize(), input.getPuzzle(), input.getStrings());
            if (solver.solve()) {
                Helper.printMatrix(solver.getSolution());
            }
            else {
                System.out.println("NO");
            }
        }catch (Exception e){
            System.out.println("NO");
        }
    }

    private static void runBenchmark() {
        String directory = System.getProperty("user.dir") + "/src/CPCFiles/";
        List<String> files = Arrays.asList("simple01.CPC","simple02.CPC","test01.CPC","test02.CPC");

        for (String file : files) {
            System.out.println(file);
            String filepath = directory + file;
            try {
                FileReader fileReader = new FileReader(filepath);
                final long decoderStartTime = System.currentTimeMillis();
                Decoder input = new Decoder(fileReader);
                final long decoderEndTime = System.currentTimeMillis();
                final long solverStartTime = System.currentTimeMillis();
                Solver solver = new Solver(input.getPuzzleSize(), input.getPuzzle(), input.getStrings());
                System.out.println("Solved: " + solver.solve());
                final long solverEndTime = System.currentTimeMillis();
                System.out.println("Loops: " + solver.getLoops());
                System.out.println("Decoder execution time: " + (decoderEndTime - decoderStartTime));
                System.out.println("Solver execution time: " + (solverEndTime - solverStartTime) + "\n");
            } catch (Exception e) {
                System.out.println("NO");
                e.printStackTrace();
            }
        }
    }
}
