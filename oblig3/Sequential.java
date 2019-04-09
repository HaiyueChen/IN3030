import java.util.Arrays;
import java.util.LinkedList;

/**
 * Sequential
 */
public class Sequential {

    private SequentialSieve sieve;
    private Oblig3Precode writer;
    private int n;
    private int[] primes;
    private long[] to_factor = new long[100];

    public static Sequential init(int n){
        if(n < 16){
            throw new UnsupportedOperationException("N must be greater than or equal to 16");
        }
        return new Sequential(n);
    }

    private Sequential(int n){
        this.n = n;
        long upper_limit = (long)n * n;
        this.writer = new Oblig3Precode(n);
        this.sieve = new SequentialSieve(n);
        this.primes = this.sieve.findPrimes();
        
        for (int i = 0; i < 100; i++) {
            this.to_factor[i] = (long) -- upper_limit;
        }
        // System.out.println(Arrays.toString(primes));
    }

    public Oblig3Precode factorize(){
        for (int i = 0; i < to_factor.length; i++) {
            long base = to_factor[i];
            long to_do = base;
            
            int search_from = 0;
            while (to_do > 1) {
                boolean found = false;
                for (int j = search_from; j < this.primes.length; j++) {
                    if(to_do % this.primes[j] == 0){
                        this.writer.addFactor(base, (long)primes[j]);
                        found = true;
                        to_do /= primes[j];
                        search_from = j;
                        break;
                    }
                }
                if(!found){
                    this.writer.addFactor(base, (long)to_do);
                    to_do = 1;
                }
            }
        }
        return this.writer;
    }

    //Exactly the same method as the one above, but with printing of base and
    //factors to the terminal. This is used for piping to the check.py script
    public Oblig3Precode factorize_with_print(){
        for (int i = 0; i < to_factor.length; i++) {
            long base = (long) to_factor[i];

            /////////////////////////////
            System.out.print((long) base + ":");
            /////////////////////////////

            long to_do = base;
            LinkedList<Long> factors = new LinkedList<>();
            
            int search_from = 0;
            while (to_do > 1) {
                boolean found = false;
                // if (search_from >= primes.length) {
                //     System.out.println("U fucked up");
                //     return null;
                // }
    
                for (int j = search_from; j < this.primes.length; j++) {
                    if(to_do % this.primes[j] == 0){

                        /////////////////////////////
                        factors.add((long)primes[j]);
                        /////////////////////////////
                        
                        this.writer.addFactor(base, (long)primes[j]);
                        found = true;
                        to_do /= primes[j];
                        search_from = j;
                        break;
                    }
                }
                if(!found){
                    this.writer.addFactor(base, (long)to_do);
                    factors.add((long)to_do);
                    to_do = 1;
                }
            }
            /////////////////////////////
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < factors.size() - 1; j++) {
                sb.append(String.format("%d*", factors.get(j)));

            }
            sb.append(String.format("%d", factors.get(factors.size() - 1)));
            System.out.println(sb.toString());   
            /////////////////////////////
        }
        return this.writer;
    }




}