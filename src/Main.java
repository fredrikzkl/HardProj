import java.io.FileReader;
import java.io.IOException;


public class Main {

    public static void main(String [ ] args)
    {
        String directory = System.getProperty("user.dir") + "/src/CPCFiles/";
        String filepath = directory + "simple01.CPC";

        try{
            FileReader file = new FileReader(filepath);
            Decoder input = new Decoder(file);
            Helper.printMatrix(input.getPuzzle());
        }catch (IOException e){
            System.out.println("File with path: '" + filepath  + "' could not be found.\n\n" + e);
        }



    }
}
