import java.util.Arrays;

/**
 * Sequential
 */
public class Sequential {

    public static double[][] transpose(double[][] matrix){
        double[][] transposed = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                transposed[i][j] = matrix[j][i];
            }
        }
        return transposed;
    }

    public static double[][] multiply_normal(double[][] a, double[][] b){
        double[][] product = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                for (int k = 0; k < a.length; k++) {
                    product[i][j] += a[i][k] * b[k][j];
                    
                }
            }
        }
        return product;
    }

    public static double[][] multiply_a_transposed(double[][] a, double[][] b){
        double[][] product = new double[a.length][a.length];
        double[][] trans_a = Sequential.transpose(a);

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                for (int k = 0; k < a.length; k++) {
                    product[i][j] += trans_a[k][i] * b[k][j];
                }
            }
        }
        return product;
    }

    public static double[][] multiply_b_transposed(double[][] a, double[][] b){
        double[][] product = new double[a.length][a.length];
        double[][] trans_b = Sequential.transpose(b);
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                for (int k = 0; k < a.length; k++) {
                    product[i][j] += a[i][k] * trans_b[j][k];
                }
            }
        }
        return product;
    }

}
