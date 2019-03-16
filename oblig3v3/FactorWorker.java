import java.util.ArrayList;
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
        // System.out.println(Arrays.toString(this.primes_to_check));
        this.m = m;
        m.total_threads ++;
        m.working_threads ++;
    }

    @Override
    public void run(){
        long work = 0;
        long old_task = -1;
        // m.working_threads ++;
        while (true) {
            work = m.get_work(old_task);
            
            // System.out.printf("ID: %d  work: %d\n", this.id, work);
            if(work == 0){
                // System.out.println("FINISHED");
                break;
            }
            ArrayList<Long> factors = new ArrayList<>();
            long to_do = work;
            int search_from = 0;
            while (to_do > 1) {
                boolean found = false;
                for (int j = search_from; j < this.primes_to_check.length; j++) {
                    if(this.primes_to_check[j] == 0){
                        break;
                    }
                    if(to_do % this.primes_to_check[j] == 0){
                        found = true;
                        to_do /= this.primes_to_check[j];
                        factors.add((long) this.primes_to_check[j]);
                        search_from = j;
                        break;
                    }
                }
                if(!found){
                    to_do = 1;
                }
            }
            m.add_factor(work, factors, id);
        }
        // m.working_threads --;
        // System.out.printf("FINISH id: %d\n", id);
        



    }
}