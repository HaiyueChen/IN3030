import java.util.Arrays;

/**
 * FactorWorker
 */
public class FactorWorker implements Runnable {
    public int id; 
    public int[] primes_to_check;
    public FactorMonitor m;

    public FactorWorker(int id,
                        int[] primes_to_check,
                        FactorMonitor m
                        ){
        this.id = id;
        this.primes_to_check = primes_to_check;
        System.out.println(Arrays.toString(this.primes_to_check));
        this.m = m;
        m.total_threads ++;
        
    }

    @Override
    public void run(){
        long work = 0;
        long old_task = -1;
        m.working_threads ++;
        while (true) {
            work = m.get_work(old_task);
            
            // System.out.printf("ID: %d  work: %d\n", this.id, work);
            if(work == 0){
                break;
            }
            boolean found_factor = false;
            for (int i = 0; i < primes_to_check.length; i++) {
                int prime = primes_to_check[i];

                if(prime == 0){
                    break;
                }
                if(work % prime == 0){
                    // System.out.printf("ID: %d  work: %d factor: %d\n", this.id, work, prime);
                    m.add_factor(work, prime);
                    found_factor = true;
                    break;
                }
            }
            old_task = work;
            if(!found_factor){
                m.no_factor(old_task);
            }
        }
        m.working_threads --;
        System.out.printf("FINISH id: %d\n", id);
        



    }
}