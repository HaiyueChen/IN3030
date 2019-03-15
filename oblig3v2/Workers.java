import java.util.concurrent.atomic.AtomicIntegerArray;

class SieveWorker implements Runnable{
    public byte[] byteArray;
    public int[] initial_primes = new int[]{2,3,5,7};
    public int value_start;
    public int value_end;

    public int id;

    public SieveWorker(int id,
                    //    int[] initial_primes,
                       AtomicIntegerArray initial_primes,
                       byte[] byteArray,
                    //    boolean[] array,
                       int value_start,
                       int value_length){
        this.id = id;
        this.byteArray = byteArray;
        // this.initial_primes = initial_primes;
        this.value_start = value_start;
        this.value_end = value_start + value_length;
        // System.out.println(String.format("id: %d  v_start: %d  v_end: %d ", id, value_start, value_end));
    }

    @Override
    public void run(){
        for (int i = 1; i < initial_primes.length; i++) {
            int new_prime = initial_primes[i];
            // int new_prime = initial_primes.get(i);
            traverse(new_prime);
        }
    }

    private void flip(int i) {
        if (i % 2 == 0) {
            return;
        }

        int byteCell = i / 16;
        int bit = (i / 2) % 8;
        this.byteArray[byteCell] |= (1 << bit);
            
    }

    private void traverse(int p) {
        int rest = this.value_start % p; 
        int start = this.value_start;

        if(start == p){
            start += p * 2;
        }
        else if(rest == 0){
            if(start % 2 == 0){
                start += p;
            }
        }
        else{
            if(start < p){
                start = p;
                start += p * 2;
            }
            else{
                start += (p - rest);
                if(start % 2 == 0){
                    start += p;
                }
            }
        }

        // System.out.printf("ID: %d , p: %d start: %d\n", this.id, p, start);
        for (int i = start; i < this.value_end; i += p * 2) {
            flip(i);
        }
    }

}
