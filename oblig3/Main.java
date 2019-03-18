import java.util.Arrays;
import java.util.LinkedList;
/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
               
        // System.out.println(Arrays.toString(args));
        if (!(args.length == 2) && !(args.length == 3)) {
            System.out.println("The correct way to use this program:");
            System.out.println("\tjava Main {N} {number of threads} {test flag}");
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

        if(args.length == 3){
            String test_flag = args[2];
            if(test_flag.equals("test-primes")){
                test_primes(n, k);
            }
            else if(test_flag.equals("test-seq")){
                test_seq_factorization(n);
            }
            else if(test_flag.equals("test-para")){
                test_para_factorization(n, k);
            }
            else{
                System.out.println("Did not recognize the token: " + test_flag);
                System.exit(0);
            }
        }
        else{
            System.out.println("------------------------------");
            System.out.println("Sequential sieve performace: ");
            double[] seq_sieve_times = new double[7];
            for (int i = 0; i < 7; i++) {
                long seq_sieve_start = System.nanoTime();
                SequentialSieve seq = new SequentialSieve(n);
                int[] primes = seq.findPrimes();
                double seq_sieve_total = (double) (System.nanoTime() - seq_sieve_start) / 1000000;
                seq_sieve_times[i] = seq_sieve_total;
                System.out.printf("Run nr %d: %f ms\n", i, seq_sieve_total);
                System.gc();
            }
            
            System.out.println("------------------------------");
            System.out.println("Parallel sieve performace: ");
            double[] para_sieve_times = new double[7];
            for (int i = 0; i < 7; i++) {
                long para_sieve_start = System.nanoTime();
                ParaSieve para = new ParaSieve(n, k);
                int[] primes = para.get_primes();
                double para_sieve_total = (double) (System.nanoTime() - para_sieve_start) / 1000000;
                para_sieve_times[i] = para_sieve_total;
                System.out.printf("Run nr %d: %f ms\n", i, para_sieve_total);
                System.gc();
            }
            
            Arrays.sort(seq_sieve_times);
            Arrays.sort(para_sieve_times);
            System.out.println("\nMedians:");
            System.out.println("Sequential: " + seq_sieve_times[4] + " ms");
            System.out.println("Parallel: " + para_sieve_times[4] + " ms");
            System.out.println("Speed up ratio: " + (double) ((seq_sieve_times[4] / para_sieve_times[4]) - 1));
            System.gc();



            System.out.println("------------------------------");
            System.out.println("Sequential factorization performace: ");
            double[] seq_factor_times = new double[7];
            for (int i = 0; i < 7; i++) {
                long seq_f_start = System.nanoTime();
                Sequential seq = Sequential.init(n);
                Oblig3Precode writer_seq = seq.factorize();
                double seq_f_total = (double) (System.nanoTime() - seq_f_start) / 1000000;
                seq_factor_times[i] = seq_f_total;
                System.out.printf("Run nr %d: %f ms\n", i, seq_f_total);
                System.gc();
            }


            System.out.println("\n------------------------------");
            System.out.println("Parallel factorization performace: ");
            double[] para_factor_times = new double[7];
            for (int i = 0; i < 7; i++) {
                long para_f_start = System.nanoTime();
                Parallel para = Parallel.init(n, 8);
                Oblig3Precode writer_para =  para.factorize();
                double para_f_total = (double) (System.nanoTime() - para_f_start) / 1000000;
                para_factor_times[i] = para_f_total;
                System.out.printf("Run nr %d: %f ms\n", i, para_f_total);
                System.gc();
            }
            Arrays.sort(seq_factor_times);
            Arrays.sort(para_factor_times);
            System.out.println("\nMedians:");
            System.out.println("Sequential: " + seq_factor_times[4] + " ms");
            System.out.println("Parallel: " + para_factor_times[4] + " ms");
            System.out.println("Speed up ratio: " + (double) ((seq_factor_times[4] / para_factor_times[4]) - 1));
        }



    }

    public static void test_primes(int n, int k){
        SequentialSieve seq = new SequentialSieve(n);
        int[] seq_primes = seq.findPrimes();

        ParaSieve para = new ParaSieve(n, k);
        int[] para_primes = para.get_primes();

        boolean correct = Arrays.equals(seq_primes, para_primes);
        System.out.println("Primes correct: " + correct);
    }

    public static void test_seq_factorization(int n){
        Sequential seq = Sequential.init(n);
        Oblig3Precode writer = seq.factorize_with_print();
        writer.writeFactors();
    }

    public static void test_para_factorization(int n, int k){
        Parallel para = Parallel.init(n, k);
        Oblig3Precode writer = para.factorize_with_print();
        writer.writeFactors();
    }

}
