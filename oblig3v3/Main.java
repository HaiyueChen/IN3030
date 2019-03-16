import java.util.Arrays;
import java.util.LinkedList;
/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
               
        // System.out.println(Arrays.toString(args));
        if (args.length < 2) {
            System.out.println("The correct way to use this program:");
            System.out.println("\tjava Main {N} {number of threads}");
            System.exit(0);
        }
        
        int n = -1;
        int k = -1;

        try {
            n = Integer.valueOf(args[0]);
        } catch (Exception e) {
            System.out.println("The correct way to use this program:");
            System.out.println("\tjava Main {N} {number of threads}");
            System.out.println("You provided the following argument for N: " + args[0]);
            System.exit(0);
        }

        if (n < 16) {
           System.out.println("N must be equal to or greater than 16");
           System.out.println(n + " was given");
           System.exit(0); 
        }

        try {
            k = Integer.valueOf(args[1]);
        } catch (Exception e) {
            System.out.println("The correct way to use this program:");
            System.out.println("\tjava Main {n} {number of threads}");
            System.out.println("You provided the following argument for k: " + args[0]);
            System.exit(0);
        }

        if (k < 1) {
            int available_threads = Runtime.getRuntime().availableProcessors(); 
            System.out.println("Number of threads provided: " + k);
            System.out.println("Defaulting to number of threads currently availiable: " + available_threads);
            k = available_threads;
        }


        // double[] seq_times = new double[7];
        // for (int i = 0; i < 7; i++) {
        //     long seq_f_start = System.nanoTime();
        //     Sequential seq = Sequential.init(2000000000);
        //     Oblig3Precode writer_seq = seq.factorize();
        //     double seq_f_total = (double) (System.nanoTime() - seq_f_start) / 1000000;
        //     seq_times[i] = seq_f_total;
        // }
        // Arrays.sort(seq_times);
        // System.out.println(seq_times[4]);

        // double[] para_times = new double[7];
        // for (int i = 0; i < 7; i++) {
        //     // System.out.println(i);
        //     long para_f_start = System.nanoTime();
        //     Parallel para = Parallel.init(2000000000, 8);
        //     Oblig3Precode writer_para =  para.factorize();
        //     double para_f_total = (double) (System.nanoTime() - para_f_start) / 1000000;
        //     para_times[i] = para_f_total;
        // }
        // Arrays.sort(para_times);
        // System.out.println((double)para_times[4]);

        // System.out.println("Ratio: " + seq_times[4] / para_times[4]);
    }

    public static void test_primes(int n, int k){
        
    }


}
