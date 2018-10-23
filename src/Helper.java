public class Helper {

    public static void printArray(char[] a){
        System.out.println(arrayToString(a));
    }

    private static String arrayToString(char[] a){
        String out = "";
        for(int i = 0 ; i  < a.length ; i++){
            out += a[i];
            if(i != a.length-1) out += ", ";
        }
        return "[" + out + "]";
    }

    public static void printMatrix(char[][] m){
        String out = "";
        int n = m.length;
        for(int x = 0 ; x < n ; x++){
            char [] tempA = new char[n];
            for(int y = 0 ; y < n ; y++){
                tempA[y] = m[x][y];
            }
            out += arrayToString(tempA) + "\n";
        }
        System.out.println(out);
    }
}
