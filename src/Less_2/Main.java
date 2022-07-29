package Less_2;

import java.util.Arrays;

public class Main {

    private static final String st = "1 3 1 2\n2 3 2 2\n5 6 7 1\n3 3 1 0";

    private static final int ROWS = 4;
    private static final int COLS = 4;

    public static String[][] transformation(String value) {
        String[] rows = value.split("\n");
        if (rows.length != ROWS)
            throw new IllegalArgumentException(rows.length + ":\n" + value);

        String[][] result = new String[ROWS][];
        for (int i = 0; i < result.length; i++) {
            result[i] = rows[i].split(" ");
            if (result[i].length != COLS)
                throw new IllegalArgumentException(result[i].length + ":\n" + value);
        }
        return result;
    }

    private static float calcMatrix(String[][] matrix) {
        int result = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                try {
                    result += Integer.parseInt(matrix[i][j]);
                } catch (NumberFormatException e) {
                    throw new NumberFormatException(matrix[i][j]);
                }
            }
        }
        return result / 2f;
    }

    public static void main(String[] args) {

    }
}


