import java.util.Arrays;
import java.util.LinkedList;
/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        boolean found_wrong = false;
        int iterations = 0;
        while (!found_wrong) {
            long seq_start_time = System.nanoTime();
            SequentialSieve seq = new SequentialSieve(100);
            int[] seq_primes = seq.findPrimes();
            long seq_total_time = (System.nanoTime() - seq_start_time) / 1000000;
            // Oblig3Precode writer = seq.factorize_with_print();
            // if(writer != null){
            //     //writer.writeFactors();
            // }
            long para_start_time = System.nanoTime();
            ParaSieve ps = new ParaSieve(100, 8);
            int[] para_primes = ps.get_primes();
            long para_total_time = (System.nanoTime() - para_start_time) / 1000000;
            System.out.println("Speed up: " + (double) seq_total_time / para_total_time);
            System.out.println(String.format("Seq time: %d \nPara time: %d\n ", seq_total_time, para_total_time));
    
            // System.out.println(Arrays.equals(para_primes, seq_primes));
            int para_index = 0;
            for (int i = 0; i < seq_primes.length; i++) {
                if(seq_primes[i] != para_primes[para_index]){
                    System.out.println(String.format("Should be: %d, got: %d",seq_primes[i], para_primes[i]));
                    found_wrong = true;
                    para_index ++;
                }
                para_index++;
            }
            iterations ++;
            if(found_wrong){
                System.out.println(Arrays.toString(seq_primes));
                System.out.println(Arrays.toString(para_primes));
            }
        }
        System.out.println(iterations);
    }

}
