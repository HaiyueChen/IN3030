import java.util.Arrays;
import java.util.LinkedList;
/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
               
        // Sequential seq = Sequential.init(2000000000);
        // Oblig3Precode writer = seq.factorize_with_print();
        // if(writer != null){
        //     //writer.writeFactors();
        // }
        
        ParaSieve ps = new ParaSieve(100, 4);
        int[] primes = ps.get_primes();
        System.out.println(Arrays.toString(primes));
    }

}
