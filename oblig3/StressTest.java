import java.util.Arrays;

/**
 * StressTest
 */
public class StressTest {

    public static void main(String[] args) {
        for (int i = 16; i < 2000000000; i++) {
            SequentialSieve seqs = new SequentialSieve(i);
            int[] seq_primes = seqs.findPrimes();
            for (int j = 1; j < 9; j++) {
                System.out.printf("Threads: %d  n: %d\n", j, i);
                ParaSieve ps = new ParaSieve(i, j);
                int[] para_primes = ps.get_primes();
                boolean correct = Arrays.equals(seq_primes, para_primes);
                if(!correct){
                    System.out.printf("NOT CORRECT  n= %d, thread count: %d\n");
                }   
            }   
            
        }
    }
}