import java.util.Arrays;

public class SieveWorker implements Runnable{
    public int id;
    public SieveMonitor m;
    public byte[] byteArray;
    public int[] work;
    public int value_start;
    public int value_end;
    public int num_primes;

    public SieveWorker(int id,
                       SieveMonitor m,
                       int value_start,
                       int value_end,
                       int[] work
                       ){
        this.id = id;
        this.m = m;
        m.total_threads ++;
        this.byteArray = m.byteArray;
        this.work = work;
        this.value_start = value_start;
        this.value_end = value_end;
        // System.out.printf("ID: %d val_s: %d  val_e: %d  work: %s\n", id, value_start, value_end, Arrays.toString(work));
        // System.out.println(Arrays.toString(work));
    }

    @Override
    public void run(){
        for (int i = 1; i < work.length; i++) {
            int prime = work[i];
            traverse(prime);
        }
        m.done_phase_one();
        for(int i = this.value_start; i <= this.value_end; i++){
            if(m.isPrime(i)){
                this.num_primes ++;
            }
        }
        // System.out.printf("ID: %d val_s: %d  val_e: %d  found primes: %d", id, value_start, value_end, );
        m.prime_count.getAndAdd(num_primes);
    }

    private void flip(int i) {
        if (i % 2 == 0) {
            return;
        }

        int byteCell = i / 16;
        int bit = (i / 2) % 8;
        byteArray[byteCell] |= (1 << bit);
    }

    private void traverse(int p) {
        int start = this.value_start;
        if (start <= p) {
            // System.out.printf("ID: %d traversing: %d start: %d\n", id, p, p * p);
            for (int i = p * p; i <= this.value_end; i += p * 2) {
                flip(i);
            }
            return;            
        }


        int rest = start % p;
        if (rest != 0) {
            start += (p - rest);
        }
        // System.out.printf("ID: %d traversing: %d start: %d\n", id, p, start);
        for (int i = start; i <= this.value_end; i += p) {
            flip(i);
        }
    }

}
