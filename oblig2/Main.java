import java.util.Arrays;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        // show_correct();
        if(args.length < 2){
            System.out.println("Correct way to run this program:");
            System.out.println("\tjava Main {seed} {n}(optional) test(optional)");
            System.exit(0);
        }
        
        int seed = Integer.valueOf(args[0]);
        int n = Integer.valueOf(args[1]);
        if(args.length == 3){
            if(args[2].equals("test")){
                show_correct(seed, n);

            }
            else{
                System.out.println("Did not recognize token: " + args[2]);
            }
            System.exit(0);
        }
        int[] sizes = new int[] { 100, 200, 500, 1000 };

        long[][] seq_normal_times = new long[sizes.length][7];
        long[][] seq_a_trans_times = new long[sizes.length][7];
        long[][] seq_b_trans_times = new long[sizes.length][7];

        long[][] para_normal_times = new long[sizes.length][7];
        long[][] para_a_trans_times = new long[sizes.length][7];
        long[][] para_b_trans_times = new long[sizes.length][7];

        
        // Gather runtime for sequential normal
        System.out.println("Start sequential normal");
        for (int i = 0; i < sizes.length; i++) {
            for (int j = 0; j < 7; j++) {
                double[][] a = Oblig2Precode.generateMatrixA(seed, sizes[i]);
                double[][] b = Oblig2Precode.generateMatrixB(seed, sizes[i]);

                long start = System.nanoTime();
                double[][] result = Sequential.multiply_normal(a, b);
                long total = (System.nanoTime() - start);
                seq_normal_times[i][j] = total / 1000000;
            }
        }

        // Gather runtime for sequential a transposed
        System.out.println("Start sequential a transposed");
        for (int i = 0; i < sizes.length; i++) {
            for (int j = 0; j < 7; j++) {
                double[][] a = Oblig2Precode.generateMatrixA(seed, sizes[i]);
                double[][] b = Oblig2Precode.generateMatrixB(seed, sizes[i]);

                long start = System.nanoTime();
                double[][] result = Sequential.multiply_a_transposed(a, b);
                long total = (System.nanoTime() - start);
                seq_a_trans_times[i][j] = total / 1000000;
            }
        }

        // Gather runtime for sequential b transposed
        System.out.println("Start sequential b transposed");
        for (int i = 0; i < sizes.length; i++) {
            for (int j = 0; j < 7; j++) {
                double[][] a = Oblig2Precode.generateMatrixA(seed, sizes[i]);
                double[][] b = Oblig2Precode.generateMatrixB(seed, sizes[i]);

                long start = System.nanoTime();
                double[][] result = Sequential.multiply_b_transposed(a, b);
                long total = (System.nanoTime() - start);
                seq_b_trans_times[i][j] = total / 1000000;
            }
        }

        // Gather runtime for parallel normal
        System.out.println("Start parallel normal");
        for (int i = 0; i < sizes.length; i++) {
            for (int j = 0; j < 7; j++) {
                double[][] a = Oblig2Precode.generateMatrixA(seed, sizes[i]);
                double[][] b = Oblig2Precode.generateMatrixB(seed, sizes[i]);

                long start = System.nanoTime();
                double[][] result = Parallel.multiply_normal(a, b);
                long total = (System.nanoTime() - start);
                para_normal_times[i][j] = total / 1000000;
            }
        }
        
        // Gather runtime for parallel a transposed
        System.out.println("Start parallel a transposed");
        for (int i = 0; i < sizes.length; i++) {
            for (int j = 0; j < 7; j++) {
                double[][] a = Oblig2Precode.generateMatrixA(seed, sizes[i]);
                double[][] b = Oblig2Precode.generateMatrixB(seed, sizes[i]);

                long start = System.nanoTime();
                double[][] result = Parallel.multiply_a_transposed(a, b);
                long total = (System.nanoTime() - start);
                para_a_trans_times[i][j] = total / 1000000;
            }
        }
        
        // Gather runtime for parallel b transpoed
        System.out.println("Start parallel b transposed");
        for (int i = 0; i < sizes.length; i++) {
            for (int j = 0; j < 7; j++) {
                double[][] a = Oblig2Precode.generateMatrixA(seed, sizes[i]);
                double[][] b = Oblig2Precode.generateMatrixB(seed, sizes[i]);

                long start = System.nanoTime();
                double[][] result = Parallel.multiply_b_transposed(a, b);
                long total = (System.nanoTime() - start);
                para_b_trans_times[i][j] = total / 1000000;
            }
        }
        System.out.println("\nSequential normal time");
        for (int i = 0; i < sizes.length; i++) {
            System.out.printf("size: %dx%d  time: %d\n", sizes[i], sizes[i], get_median(seq_normal_times[i]));
        }

        System.out.println("\nSequential a transposed time");
        for (int i = 0; i < sizes.length; i++) {
            System.out.printf("size: %d x %d  time: %d\n", sizes[i], sizes[i], get_median(seq_a_trans_times[i]));
        }

        System.out.println("\nSequential b transosed time");
        for (int i = 0; i < sizes.length; i++) {
            System.out.printf("size: %d x %d  time: %d\n", sizes[i], sizes[i], get_median(seq_b_trans_times[i]));
        }

        System.out.println("\nParallel normal time");
        for (int i = 0; i < sizes.length; i++) {
            System.out.printf("size: %d x %d  time: %d\n", sizes[i], sizes[i], get_median(para_normal_times[i]));
        }

        System.out.println("\nParallel a transposed time");
        for (int i = 0; i < sizes.length; i++) {
            System.out.printf("size: %d x %d  time: %d\n", sizes[i], sizes[i], get_median(para_a_trans_times[i]));
        }

        System.out.println("\nParallel b transposed time");
        for (int i = 0; i < sizes.length; i++) {
            System.out.printf("size: %d x %d  time: %d\n", sizes[i], sizes[i], get_median(para_b_trans_times[i]));
        }


    }

    public static void show_correct(int seed, int n) {
        // Generates two matrixes with the precode
        double[][] a = Oblig2Precode.generateMatrixA(seed, n);
        double[][] b = Oblig2Precode.generateMatrixB(seed, n);

        // Use result from normal sequential algorithm as expected value
        double[][] result_sequential_normal = Sequential.multiply_normal(a, b);

        // Generate results from sequential implementations with transposed matrixes
        double[][] result_sequential_a_trans = Sequential.multiply_a_transposed(a, b);
        double[][] result_sequential_b_trans = Sequential.multiply_b_transposed(a, b);
        // Generate results from all parallel implementations
        double[][] result_para_normal = Parallel.multiply_normal(a, b);
        double[][] result_para_a_trans = Parallel.multiply_a_transposed(a, b);
        double[][] result_para_b_trans = Parallel.multiply_b_transposed(a, b);

        // Test sequential results
        System.out.println("Sequential a transposed correct: "
                + Arrays.deepEquals(result_sequential_normal, result_sequential_a_trans));
        System.out.println("Sequential b transposed correct: "
                + Arrays.deepEquals(result_sequential_normal, result_sequential_b_trans));
        // Test parallel results
        System.out.println(
                "\nParallel normal correct: " + Arrays.deepEquals(result_sequential_normal, result_para_normal));
        System.out.println(
                "Parallel a transposed correct: " + Arrays.deepEquals(result_sequential_normal, result_para_a_trans));
        System.out.println(
                "Parallel b transposed correct: " + Arrays.deepEquals(result_sequential_normal, result_para_b_trans));

        System.out.println("-------- Saving results --------");

        Oblig2Precode.saveResult(seed, Oblig2Precode.Mode.SEQ_NOT_TRANSPOSED, result_sequential_normal);
        Oblig2Precode.saveResult(seed, Oblig2Precode.Mode.SEQ_A_TRANSPOSED, result_sequential_a_trans);
        Oblig2Precode.saveResult(seed, Oblig2Precode.Mode.SEQ_B_TRANSPOSED, result_sequential_b_trans);

        Oblig2Precode.saveResult(seed, Oblig2Precode.Mode.PARA_NOT_TRANSPOSED, result_para_normal);
        Oblig2Precode.saveResult(seed, Oblig2Precode.Mode.PARA_A_TRANSPOSED, result_para_a_trans);
        Oblig2Precode.saveResult(seed, Oblig2Precode.Mode.PARA_B_TRANSPOSED, result_para_b_trans);
        
        System.out.println("------------- DONE -------------");
    }

    public static long get_median(long[] array){
        Arrays.sort(array);
        return array[array.length / 2];
    }
}