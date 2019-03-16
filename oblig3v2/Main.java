import java.util.Arrays;
import java.util.LinkedList;
/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
               
        // Sequential seq = Sequential.init(2000000000);
        // Oblig3Precode writer = seq.factorize_with_print();
        // if(writer != null){
        //     //writer.writeFactors();
        // }
        /*
        long[] para_times = new long[7];
        long[] seq_times = new long[7];
        for (int i = 0; i < 7; i++) {
            // Runtime runtime = Runtime.getRuntime();
            long seq_start = System.nanoTime();
            SequentialSieve seqs = new SequentialSieve(200000000);
            int[] seq_primes = seqs.findPrimes();
            long seq_total = System.nanoTime() - seq_start;
            
            long para_start = System.nanoTime();
            ParaSieve ps = new ParaSieve(200000000, 8);
            int[] primes = ps.get_primes();
            long para_total = System.nanoTime() - para_start;
            
            // double allocatedMemory = (double)((runtime.totalMemory() - runtime.freeMemory())/ (1024 * 1024));
            // System.out.println(allocatedMemory);
            para_times[i] = para_total;
            seq_times[i] = seq_total;
        }
        Arrays.sort(para_times);
        Arrays.sort(seq_times);
        System.out.println("Seq median: " + (double)seq_times[4] / 1000000);
        System.out.println("Para median: " + (double)para_times[4] / 1000000);
        System.out.println("Speed up: " + (((double) seq_times[4] / para_times[4]) - 1));
        */

        double[] seq_times = new double[7];
        for (int i = 0; i < 7; i++) {
            long seq_f_start = System.nanoTime();
            Sequential seq = Sequential.init(2000000000);
            Oblig3Precode writer_seq = seq.factorize();
            double seq_f_total = (double) (System.nanoTime() - seq_f_start) / 1000000;
            seq_times[i] = seq_f_total;
        }
        Arrays.sort(seq_times);
        System.out.println(seq_times[4]);

        double[] para_times = new double[7];
        for (int i = 0; i < 7; i++) {
            // System.out.println(i);
            long para_f_start = System.nanoTime();
            Parallel para = Parallel.init(2000000000, 8);
            Oblig3Precode writer_para =  para.factorize();
            double para_f_total = (double) (System.nanoTime() - para_f_start) / 1000000;
            para_times[i] = para_f_total;
        }
        Arrays.sort(para_times);
        System.out.println((double)para_times[4]);

        System.out.println("Ratio: " + seq_times[4] / para_times[4]);
    }

}
