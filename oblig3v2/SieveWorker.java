import java.util.Arrays;

public class SieveWorker implements Runnable{
    public int id;
    public SieveMonitor m;
    public boolean[] booArray;
    public int[] work;
    public int value_start;
    public int value_end;
    public int num_primes;

    public SieveWorker(int id,
                       SieveMonitor m,
                       int value_start,
                       int value_length,
                       int[] work
                       ){
        this.id = id;
        this.m = m;
        m.total_threads ++;
        this.booArray = m.booArray;
        this.work = work;
        this.value_start = value_start;
        this.value_end = value_start + value_length;
        // System.out.printf("ID: %d val_s: %d  val_e: %d  work: \n", id, value_start, value_end);
        // System.out.println(Arrays.toString(work));
    }

    @Override
    public void run(){
        for (int i = 0; i < work.length; i++) {
            int prime = work[i];
            if(prime == 0){
                break;
            }
            traverse(prime);
        }
        m.done_phase_one();
        for(int i = this.value_start; i < this.value_end; i ++){
            if(m.isPrime(i)){
                this.num_primes ++;
            }
        }
        // System.out.printf("ID: %d val_s: %d  val_e: %d  found primes: %d", id, value_start, value_end, );
        m.prime_count.getAndAdd(num_primes);
    }

    private void flip(int i) {
        if( (i % 2) == 0){
            return;
        }
        int cell = i / 2;
        this.booArray[cell] = true;
    }

    private void traverse(int p) {
        for (int i = p*p; i <= this.booArray.length * 2; i += p * 2) {
            flip(i);
        }
    }

}
