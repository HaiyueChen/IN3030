import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ParaSieve {

    public int n;
    public int thread_count;
    public SieveMonitor monitor;

    public ParaSieve(int n, int thread_count) {
        this.n = n;
        if(thread_count > n){
            this.thread_count = n - 1;    
        }
        this.thread_count = thread_count;
        this.monitor = new SieveMonitor(this.n);

    }
    
    private void start() {
        
        Thread[] threads = new Thread[this.thread_count];
        int value_start = 3;
        int value_length = n / this.thread_count;
        
        // int work_size = (this.monitor.initial_primes.length / thread_count) + 1;
        // int[][] all_the_work = new int[thread_count][work_size];
        // int primes_index = 0;
        // for (int i = 0; i < work_size; i++) {
        //     for (int j = 0; j < thread_count; j++) {
        //         if(primes_index == this.monitor.initial_primes.length){
        //             all_the_work[j][i] = 0;
        //         }
        //         else{
        //             int prime_number = this.monitor.initial_primes[primes_index];
        //             all_the_work[j][i] = prime_number;
        //             primes_index ++;

        //         }
        //     }
        // }

        for (int i = 0; i < this.thread_count - 1; i++) {
            int value_end = value_start + value_length;
            int rest = value_end % 16;
            value_end += 16 - rest;
            if (value_end > n) {
                value_end = n;
            }
            // int[] work = all_the_work[i];
            threads[i] = new Thread(new SieveWorker(i,
                                    this.monitor, 
                                    value_start, 
                                    value_end,
                                    this.monitor.initial_primes
                                    ));
            value_start = value_end + 1;
        }
        // int[] work = all_the_work[this.thread_count - 1];
        threads[this.thread_count - 1] = new Thread(new SieveWorker(this.thread_count - 1,
                                                                    this.monitor, 
                                                                    value_start, 
                                                                    n,
                                                                    this.monitor.initial_primes
                                                                    ));
        
        for (int i = 0; i < this.thread_count; i++) {
            threads[i].start();
        }

        for (int i = 0; i < this.thread_count; i++) {
            try { threads[i].join(); } catch (InterruptedException e) {}
        }
    }

    public int[] get_primes() {
        this.start();
        return monitor.get_primes();
    }

}


class SieveMonitor{
    public byte[] byteArray;
    public int n;
    public int squareRootN;
    public int[] initial_primes;


    public int currentPrime_index = 1;
    public final Lock lock = new ReentrantLock();
    public AtomicInteger prime_count = new AtomicInteger(0);
    public final Condition wait_for_all_finish = lock.newCondition();
    
    public int total_threads = 0;
    public int num_threads_done_phase_1 = 0;


    public SieveMonitor(int n){
        this.n = n;
        int cells = n / 16 + 1;
        this.byteArray = new byte[cells];
        this.squareRootN = (int) Math.sqrt(n) + 1;
        SequentialSieve seqSieve = new SequentialSieve(squareRootN + 1);
        this.initial_primes = seqSieve.findPrimes();
    }

    public void done_phase_one(){
        lock.lock();
        try{
            this.num_threads_done_phase_1 ++;
            if(num_threads_done_phase_1 == total_threads){
                wait_for_all_finish.signalAll();
            }
            else{
                try { wait_for_all_finish.await();} catch (Exception e) {}
            }
        }
        finally{
            lock.unlock();
        }
    }

    public int[] get_primes(){
        int num_primes = this.prime_count.get() + 1;

        int[] primes = new int[num_primes];
        primes[0] = 2;
        int currentPrime = 3;
        for (int i = 1; i < num_primes; i++) {
            primes[i] = currentPrime;
            currentPrime = findNextPrime(currentPrime + 2);
        }
        return primes;
    }
    

    private int findNextPrime(int startAt) {
        for (int i = startAt; i < n; i += 2) {
            if(isPrime(i)) {
                return i;
            }
        }
        return 0;
    }
    
    public boolean isPrime(int i){
        if((i % 2) == 0){
            return false;
        }
        int byteCell = i / 16;
        int bit = (i / 2) % 8;
        return (byteArray[byteCell] & (1 << bit)) == 0;
    }

}

