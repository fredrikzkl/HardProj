class Helper {

    static void printMatrix(char[][] m){
        StringBuilder stringBuilder = new StringBuilder();
        for (char[] row : m) {
            for (int j = 0; j < m.length; j++) {
                stringBuilder.append(row[j]);
                if (j != m.length - 1) stringBuilder.append(";");
            }
            stringBuilder.append("\n");
        }
        System.out.print(stringBuilder.toString());
    }
}
