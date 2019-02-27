import java.util.Arrays;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        int[] sizes = new int[]{100, 200, 500, 1000};
        
        int n = 3;
        double[][] a = new double[n][n];
        double[][] b = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                a[i][j] = i + 1;
                b[i][j] = i + 1;
            }
        }
        
        a = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        b = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};

        double[][] test_a = Oblig2Precode.generateMatrixA(10, sizes[3]);
        double[][] test_b = Oblig2Precode.generateMatrixB(10, sizes[3]);

        double[][] norm_test = Sequential.multiply_normal(test_a, test_b);
        double[][] para_norm_test = Parallel.multiply_b_transposed(test_a, test_b);


        System.out.println("Seq T == Para T: " + Arrays.deepEquals(norm_test, para_norm_test));
        // System.out.println(Arrays.deepToString(norm_test));
        // System.out.println(Arrays.deepToString(para_norm_test));

        // double[][] result_1 = Sequential.multiply_normal(a, b);        
        // double[][] result_2 = Sequential.multiply_a_transposed(a, b);
        // double[][] result_3 = Sequential.multiply_b_transposed(a, b);
 
        // System.out.println("Normal multiplication: \n" + Arrays.deepToString(result_1) + "\n");
        // System.out.println("Transpose a: \n" + Arrays.deepToString(result_2)+ "\n");
        // System.out.println("Transpose b: \n" + Arrays.deepToString(result_3)+ "\n");






    }
}