import java.io.InputStreamReader;

public class Main {

    public static void main(String [ ] args)
    {
        try{
            Decoder input = new Decoder(new InputStreamReader(System.in));
            Solver solver = new Solver(input);
            if (solver.solve()) {
                solver.printSolution();
            }
            else {
                System.out.println("NO");
            }
        }catch (Exception e){
            System.out.println("NO");
        }
    }
}
