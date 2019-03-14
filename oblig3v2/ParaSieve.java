import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ParaSieve {

    public int n;
    public int thread_count;
    public byte[] byteArray;
    public int[] initial_primes;

    public ParaSieve(int n, int thread_count) {
        this.n = n;
        this.thread_count = thread_count;
        
        int root_of_n = (int) Math.sqrt(n) + 1;
        SequentialSieve seqSieve = new SequentialSieve(root_of_n);
        this.initial_primes = seqSieve.findPrimes();
        System.out.println(Arrays.toString(initial_primes));
        int cells = n / 16 + 1;
        this.byteArray = new byte[cells];
    }
    
    public int[] get_primes(){
        this.start();
        int prime_counter = initial_primes.length - 1;
        for (int i = initial_primes[initial_primes.length - 1]; i < this.n; i++) {
            if(this.isPrime(i)){
                prime_counter ++;
            }
        }

        int[] primes = new int[prime_counter];
        primes[0] = 2;
        int current_prime = 3;
        for (int i = 1; i < prime_counter; i++) {
            primes[i] = current_prime;
            current_prime = this.findNextPrime(current_prime + 2);
        }

        return primes;

    }

    private void start(){
        int value_start = 3;
        int value_length = n / thread_count;
        Thread[] threads = new Thread[this.thread_count];
        for (int i = 0; i < this.thread_count - 1; i++) {
            threads[i] = new Thread(
                                    new SieveWorker(
                                        initial_primes, 
                                        this.byteArray,
                                        value_start, 
                                        value_length));

            value_start += value_length;
        }
        threads[this.thread_count - 1] = new Thread(
                                                new SieveWorker(
                                                initial_primes,
                                                this.byteArray, 
                                                value_start, 
                                                n - value_start));
        
        for (int i = 0; i < this.thread_count; i++) {
            threads[i].start();
        }

        for (int i = 0; i < this.thread_count; i++) {
            try { threads[i].join(); } catch (Exception e) {System.out.println(e);}
        }
    }

    private int findNextPrime(int startAt) {
        for (int i = startAt; i < n; i += 2) {
            if(this.isPrime(i)) {
                return i;
            }
        }
        return 0;
    }

    private boolean isPrime(int i) {
        if((i % 2) == 0) {
            return false;
        }

        int byteCell = i / 16;
        int bit = (i / 2) % 8;

        return (this.byteArray[byteCell] & (1 << bit)) == 0;
    }

}

