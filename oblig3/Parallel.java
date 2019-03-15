/**
 * Parallel
 */
public class Parallel {
    private ParaSieve paraSieve;
    private Oblig3Precode writer;
    private int n;
    private int[] primes;
    private long[] to_factor = new long[100];

    public static Parallel init(int n, int thread_count){
        if (n < 16) {
            throw new UnsupportedOperationException("N must be grater than 16");
        }

        return new Parallel(n, thread_count);
    }


    private Parallel(int n, int thread_count){
        this.paraSieve = new ParaSieve(n, thread_count);
        this.writer = new Oblig3Precode(n);
        
        long upper_limit = (long) n*n;
        for (int i = 0; i < 100; i++) {
            this.to_factor[i] = upper_limit--;
        }

    }

    public Oblig3Precode factorize(){
        this.primes = paraSieve.get_primes();


        return this.writer;
    }
    
}


/**
 * FactorMonitor
 */
class FactorMonitor {

    


    
}

