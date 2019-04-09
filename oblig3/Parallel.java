import java.util.LinkedList;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Parallel
 */
public class Parallel {
    public int thread_count;
    public ParaSieve paraSieve;
    public Oblig3Precode writer;
    public int[] primes;
    public long[] to_factor = new long[100];

    public static Parallel init(int n, int thread_count){
        if (n < 16) {
            throw new UnsupportedOperationException("N must be grater than 16");
        }

        return new Parallel(n, thread_count);
    }


    private Parallel(int n, int thread_count){
        this.thread_count = thread_count;
        this.writer = new Oblig3Precode(n);
        
        this.paraSieve = new ParaSieve(n + 2, thread_count);
        this.primes = paraSieve.get_primes();
        // System.out.println(Arrays.toString(primes));
        long upper_limit = (long) n*n;
        for (int i = 0; i < 100; i++) {
            this.to_factor[i] = -- upper_limit;
        }

    }

    public Oblig3Precode factorize_with_print(){
        TreeMap<Long, LinkedList<Long>> result_bucket = new TreeMap<>();
        for (int i = 0; i < this.to_factor.length; i++) {
            result_bucket.put(this.to_factor[i], new LinkedList<Long>());
        }
        FactorMonitor monitor = new FactorMonitor(this.to_factor, result_bucket);
        // System.out.println(Arrays.toString(monitor.to_factor));
        int table_size = this.primes.length / this.thread_count + 1;
        int[][] all_the_tables = new int[thread_count][table_size];
        int primes_index = 0;
        for (int i = 0; i < table_size; i++) {
            for (int j = 0; j < thread_count; j++) {
                if(primes_index == this.primes.length){
                    all_the_tables[j][i] = 0;
                }
                else{
                    int prime_number = this.primes[primes_index];
                    all_the_tables[j][i] = prime_number;
                    primes_index ++;
                }
            }
        }
        // System.out.println(Arrays.deepToString(all_the_tables));
        Thread[] threads = new Thread[thread_count];
        for (int i = 0; i < this.thread_count - 1; i++) {
            int[] work = all_the_tables[i];
            threads[i] = new Thread(new FactorWorker(i,
                                                    work,
                                                    monitor 
                                                    ));
        }
        int[] work = all_the_tables[this.thread_count - 1];
        threads[this.thread_count - 1] = new Thread(new FactorWorker(this.thread_count - 1,
                                                                    work,
                                                                    monitor 
                                                                    ));

        for (int i = 0; i < thread_count; i++) {
            threads[i].start();
        }

        for (int i = 0; i < thread_count; i++) {
            try { threads[i].join();} catch (Exception e) {}
        }
        for(Map.Entry<Long, LinkedList<Long>> entry : monitor.result_bucket.entrySet()) {
       
            // Starting a new line with the base
            System.out.print(entry.getKey() + ":");            
            
            // Sort the factors
            Collections.sort(entry.getValue());
            
            // Then print the factors
            String out = "";
            for(Long l : entry.getValue())
                out += l + "*";
            
            // Removing the trailing '*'
            try {
                System.out.println(out.substring(0, out.length()-1));
                
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println(out);
            }
        }


        this.writer.factors = monitor.result_bucket;

        return this.writer;
    }


    public Oblig3Precode factorize(){
        TreeMap<Long, LinkedList<Long>> result_bucket = new TreeMap<>();
        for (int i = 0; i < this.to_factor.length; i++) {
            result_bucket.put(this.to_factor[i], new LinkedList<Long>());
        }
        FactorMonitor monitor = new FactorMonitor(this.to_factor, result_bucket);
        // System.out.println(Arrays.toString(monitor.to_factor));
        int table_size = this.primes.length / this.thread_count + 1;
        int[][] all_the_tables = new int[thread_count][table_size];
        int primes_index = 0;
        for (int i = 0; i < table_size; i++) {
            for (int j = 0; j < thread_count; j++) {
                if(primes_index == this.primes.length){
                    all_the_tables[j][i] = 0;
                }
                else{
                    int prime_number = this.primes[primes_index];
                    all_the_tables[j][i] = prime_number;
                    primes_index ++;
                }
            }
        }
        // System.out.println(Arrays.deepToString(all_the_tables));
        Thread[] threads = new Thread[thread_count];
        for (int i = 0; i < this.thread_count - 1; i++) {
            int[] work = all_the_tables[i];
            threads[i] = new Thread(new FactorWorker(i,
                                                    work,
                                                    monitor 
                                                    ));
        }
        int[] work = all_the_tables[this.thread_count - 1];
        threads[this.thread_count - 1] = new Thread(new FactorWorker(this.thread_count - 1,
                                                                    work,
                                                                    monitor 
                                                                    ));
        // System.out.println("Starting threads");
        for (int i = 0; i < thread_count; i++) {
            threads[i].start();
        }
        // System.out.println("All threads started");

        for (int i = 0; i < thread_count; i++) {
            try { threads[i].join();} catch (Exception e) {}
        }
        // System.out.println("All threads done");
    
        this.writer.factors = monitor.result_bucket;

        return this.writer;
    }
    
}


/**
 * FactorMonitor
 */
class FactorMonitor {

    private final Lock lock = new ReentrantLock(true);
    private final Condition wait_for_work = lock.newCondition();
    public long[] to_factor; 
    public Long current_base;
    public int factor_index;
    // public long current_to_factor;
    public TreeMap<Long, LinkedList<Long>> result_bucket;
    public boolean started = false;
    public boolean dead_end = true;
    public int total_threads= 0;
    public int working_threads = 0;
    public int threads_exausted = 0;


    public FactorMonitor(long[] to_factor, TreeMap<Long, LinkedList<Long>> result_bucket){
        this.to_factor = to_factor;      
        this.factor_index = 0;
        this.result_bucket = result_bucket;
        this.current_base = to_factor[factor_index];
    }

    public long get_work(long prev){
        lock.lock();
        try{
            // working_threads --;
            if(!this.started){
                set_next_task();
                this.started = true;
            }

            return this.current_base;
        }
        finally{
            lock.unlock();
        }

    }

    public void add_factor(long base, ArrayList<Long> factors, int id){
        lock.lock();
        try{
            working_threads --;
            if(working_threads == 0){
                if (factors.size() > 0) {
                    for (int i = 0; i < factors.size(); i++) {
                        this.result_bucket.get(current_base).add(factors.get(i));
                    }
                }
                long rest = current_base;
                for (Long factor : this.result_bucket.get(current_base)) {
                    rest /= factor;
                }
                if(rest > 1){
                    this.result_bucket.get(current_base).add(rest);
                }
                set_next_task();
                working_threads = total_threads;
                wait_for_work.signalAll();
            }
            else{
                if(factors.size() > 0) {
                    for (int i = 0; i < factors.size(); i++) {
                        this.result_bucket.get(current_base).add(factors.get(i));
                    }
                }
                try {
                    wait_for_work.await();
                } catch (Exception e) {}
            }
        }
        finally{
            lock.unlock();
        }
    }

    private void set_next_task(){
        if(this.factor_index == this.to_factor.length){
            this.current_base = (long) 0;
            return;
        }
        this.current_base = this.to_factor[this.factor_index];
        this.factor_index ++;
    }

    


    
}

