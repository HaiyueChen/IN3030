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
        this.thread_count = thread_count;
        this.monitor = new SieveMonitor(this.n);
    }
    
    private void start() {
        
        Thread[] threads = new Thread[this.thread_count + 1];
        int value_start = 3;
        int value_length = (n - 3) / this.thread_count;
        int rest = (n - 3) % value_length;

        for (int i = 0; i < this.thread_count - 1; i++) {
            // System.out.println(String.format("%d %d", value_start, value_length));
            threads[i] = new Thread(new SieveWorker(
                                    this.monitor, 
                                    value_start, 
                                    value_length));
            value_start += value_length;
        }
        threads[this.thread_count - 1] = new Thread(new SieveWorker(this.monitor, 
                                                                    value_start, 
                                                                    value_length + rest + 1));
        // System.out.println(String.format("%d %d", value_start, value_length + rest));
        // threads[this.thread_count] = new Thread(new PrimeWorker(this.monitor));



        for (int i = 0; i < this.thread_count; i++) {
            threads[i].start();
        }
        System.out.println("Totoal threads: " + monitor.total_threads);

        for (int i = 0; i < this.thread_count; i++) {
            try { threads[i].join(); } catch (InterruptedException e) {}
        }
    }

    public int[] get_primes() {
        this.start();
        return this.monitor.get_primes();
    }

}


class SieveMonitor{
    public byte[] byteArray;
    public int n;
    public int squareRootN;

    public int currentPrime = 3;
    public final Lock lock = new ReentrantLock();
    public final Condition get_new_prime = lock.newCondition();
    public final Condition wait_for_new_prime = lock.newCondition();
    
    public int total_threads = 0;
    public int working_threads = 0;
    // public AtomicInteger total_threads = new AtomicInteger(0);
    // public AtomicInteger working_threads = new AtomicInteger(0);
    // public boolean all_running = true;



    public SieveMonitor(int n){
        this.n = n;
        int cells = n / 16 + 1;
        this.byteArray = new byte[cells];
        this.squareRootN = (int) Math.sqrt(n);
    }

    public int fetch_current_prime(int last_prime){
        lock.lock();
        try {
            
            if(last_prime == this.currentPrime){
                // if(!all_running){
                //     try {
                //         // this.working_threads.getAndDecrement();
                //         wait_for_new_prime.await();
                //         this.working_threads --;
                //     } catch (Exception e) {}
                // }
                // else{
                //     this.working_threads --;
                //     // this.working_threads.getAndDecrement();
                // }
                this.working_threads --;
                // System.out.println(String.format("%d %d", total_threads, working_threads));
                // if(this.working_threads == 0 && this.all_running){
                if(this.working_threads == 0){
                    // this.all_running = false;
                    // System.out.println("set next prime and signal all");
                    this.set_next_prime();
                    wait_for_new_prime.signalAll();
                }
                else{
                    try {
                        wait_for_new_prime.await();
                    } catch (InterruptedException e) {}
                }

                this.working_threads ++;
                if(this.working_threads != this.total_threads){
                //     System.out.println(String.format("%d %d", total_threads, working_threads));
                //     System.out.println("signaling all");
                //     all_running = true;
                //     wait_for_new_prime.signalAll();
                    try {
                        wait_for_new_prime.await();
                        
                    } catch (Exception e) {
                        //TODO: handle exception
                    }
                }
                else{
                    wait_for_new_prime.signalAll();
                }
            }
            return this.currentPrime;
        }
        finally{
            lock.unlock();
        }
    }

    public int set_next_prime() {
        this.currentPrime = findNextPrime(this.currentPrime + 2);
        // wait_for_new_prime.signalAll();
        return this.currentPrime;

    }

    // public int set_next_prime() {
    //     lock.lock();
    //     try{
    //         // System.out.println(this.working_threads);
    //         if(this.working_threads != 0){
    //             // System.out.println("Prime worker waiting");
    //             try { get_new_prime.await(); } catch (Exception e) {}
    //         }
    //         this.currentPrime = findNextPrime(this.currentPrime + 2);
    //         // System.out.println("Prime worker signingaling all Sieve workers");
    //         wait_for_new_prime.signalAll();
    //         return this.currentPrime;
    //     }
    //     finally{
    //         lock.unlock();
    //     }
    // }

    public int[] get_primes(){
        int num_primes = this.countPrimes();
        int[] primes = new int[num_primes];
        primes[0] = 2;
        int currentPrime = 3;
        for (int i = 1; i < num_primes; i++) {
            primes[i] = currentPrime;
            currentPrime = findNextPrime(currentPrime + 2);
        }
        return primes;
    }
    
    private int countPrimes() {
        int num_primes = 1;
        int start_at = 3;
        while(start_at != 0){
            // System.out.println(start_at);
            num_primes ++;
            start_at = findNextPrime(start_at + 2);
        }
        return num_primes;
    }


    private int findNextPrime(int startAt) {
        for (int i = startAt; i < n; i += 2) {
            if(isPrime(i)) {
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

        return (byteArray[byteCell] & (1 << bit)) == 0;
    }


}

