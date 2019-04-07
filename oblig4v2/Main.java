import java.util.Arrays;
import java.util.LinkedList;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        // java Main {n} {seed} {numBits} {test-flag}
        if(args.length < 3){
            System.out.println("The correct way to run this program:");
            System.out.println("\tjava Main {n} {seed} {numBits} {test}(optional)");
            System.exit(0);
        }
        
        if(args.length == 4 && args[3].equals("test")){
            System.out.println("Running tests");
            System.exit(0);
        }
        else if(args.length == 3){
            int NUM_RUNS = 1;
            int n = Integer.valueOf(args[0]);
            int seed = Integer.valueOf(args[1]);
            int numBits = Integer.valueOf(args[2]);
            
            System.out.printf("Number of runs: %d \nN: %d\nSeed: %d\nNumber of bits: %d\n", NUM_RUNS, n, seed, numBits);
            
            double[] para_times = new double[NUM_RUNS];
            double[] seq_times = new double[NUM_RUNS];
            
            int[] org = Oblig4Precode.generateArray(n, seed);
            int[] correct = org.clone();
            Arrays.sort(correct);

            System.out.println("\nRunning sequential");
            for (int i = 0; i < NUM_RUNS; i++) {
                long seq_time_start = System.nanoTime();
                int[] seq_res = Sequential.sort(org, numBits);
                seq_times[i] = (double) (System.nanoTime() - seq_time_start) / 1000000;
                System.out.printf("Sequential run: %d\tRuntime: %f ms\t", i + 1, seq_times[i]);
                System.out.println("Correct: " + Arrays.equals(seq_res, correct));
                System.gc();
            }
            System.out.println("\nRunning parallel");
            // int processors = Runtime.getRuntime().availableProcessors();
            int processors = 2;
            for (int i = 0; i < NUM_RUNS; i++) {
                long para_time_start = System.nanoTime();
                int[] para_res = Parallel.sort(org, numBits, processors);
                para_times[i] = (double) (System.nanoTime() - para_time_start) / 1000000;
                System.out.printf("Parallel   run: %d\tRuntime: %f ms\t", i + 1, para_times[i]);
                System.out.println("Correct: " + Arrays.equals(para_res, correct));
                // test_increasing(para_res);
                System.gc();
            }
    
            // Arrays.sort(para_times);
            // Arrays.sort(seq_times);
            // double seq_median = seq_times[NUM_RUNS / 2 + 1];
            // double para_median = para_times[NUM_RUNS / 2 + 1];
            // double ratio = seq_median / para_median;
            // System.out.printf("\nSequential median: %f\nParallel  median: %f\n", seq_median, para_median);
            // System.out.printf("Speed up ratio: %f\n", ratio);

        }


    }

    public static void test_increasing(int[] a){
        LinkedList<Integer[]> wrongIndices = new LinkedList<>();
        for (int i = 1; i < a.length; i++) {
            if(a[i -1] > a[i]) 
                wrongIndices.add(new Integer[]{i - 1, i});
        }
        if (wrongIndices.size() == 0){
            System.out.println("All correct");
            return;
        } 
        System.out.println("Wrong indices:");
        for (Integer[] pairs : wrongIndices){
            System.out.println(Arrays.toString(pairs));
        }
    }

    public static void findMax(int[] a){
        int max = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] > max) {
                max = a[i];
            }
        }
        System.out.println(max);
    }





}