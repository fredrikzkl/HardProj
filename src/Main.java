import java.io.FileReader;
import java.io.IOException;


public class Main {

    public static void main(String [ ] args)
    {
        String directory = System.getProperty("user.dir") + "/src/CPCFiles/";
        String filepath = directory + "test02.CPC";

        try{
            FileReader file = new FileReader(filepath);
            Decoder input = new Decoder(file);
            Helper.printMatrix(input.getPuzzle());
            if (input.checkCrossword()) {
                Helper.printMatrix(input.getNewCrossword());
            }
            else {
                System.out.println("RIP");
            }
        }catch (IOException e){
            System.out.println("File with path: '" + filepath  + "' could not be found.\n\n" + e);
        }



    }
}
