import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        long total_start = System.nanoTime();

        int n = Integer.valueOf(args[0]);
        int k = Integer.valueOf(args[1]);
        
        Long[] arrays_times = new Long[7];
        Long[] seq_times = new Long[7];
        Long[] two_threads_times = new Long[7];
        Long[] four_threads_times = new Long[7];
        Long[] eight_threads_times = new Long[7];

        int[] random_array = new int[n];
        Random rand = new Random(7361);
        for (int i = 0; i < random_array.length; i++) {
            random_array[i] = rand.nextInt(999999999);
        }
        /*
        int[] sorted_correct = new int[k];
        for (int z = 0; z < 7; z++) {
            int[] sorted = random_array.clone();
            long arrays_start = System.nanoTime();
            Arrays.sort(sorted);
            for (int i = sorted.length - 1, count = 0; count < k; i--, count ++) {
                sorted_correct[count] = sorted[i];
            }
            long arrays_time = (System.nanoTime() - arrays_start); 
            arrays_times[z] = arrays_time;
            
        }
        */

        //Sequential computing
        for (int z = 0; z < 7; z++) {
            int[] sorted = random_array.clone();
    
            long seq_start = System.nanoTime();
            sorted = Sequential.find_k_largest(sorted, k);
            long seq_time = (System.nanoTime() - seq_start);
            seq_times[z] = seq_time;
            //System.out.println("Correct? " + check_correct(sorted_correct , sorted));
            
        }

        //Parallel computing
        for (int z = 0; z < 7; z++) {
            int[] sorted = random_array.clone();
    
            long para_start = System.nanoTime();
            sorted = Parallel.find_k_largest(sorted, k, 2);
            long para_time = (System.nanoTime() - para_start);
            two_threads_times[z] = para_time; 
            //System.out.println("Correct? " + check_correct(sorted_correct , sorted));
        }
        
        for (int z = 0; z < 7; z++) {
            int[] sorted = random_array.clone();
    
            long para_start = System.nanoTime();
            sorted = Parallel.find_k_largest(sorted, k, 4);
            long para_time = (System.nanoTime() - para_start);
            four_threads_times[z] = para_time;
            //System.out.println("Correct? " + check_correct(sorted_correct , sorted));
        }
        
        for (int z = 0; z < 7; z++) {
            int[] sorted = random_array.clone();
    
            long para_start = System.nanoTime();
            sorted = Parallel.find_k_largest(sorted, k, 8);
            long para_time = (System.nanoTime() - para_start);
            eight_threads_times[z] = para_time;
            //System.out.println("Correct? " + check_correct(sorted_correct , sorted));
        }
        
        /*
        System.out.println("Arrays.sort");
        for (int i = 0; i < 7; i++) {
            System.out.println(arrays_times[i] / 1000);
        }
        */
        System.out.println("Sequantial");
        for (int i = 0; i < 7; i++) {
            System.out.println(seq_times[i] / 1000000);
        }
        System.out.println("Two threads");
        for (int i = 0; i < 7; i++) {
            System.out.println(two_threads_times[i] / 1000000);
        }
        System.out.println("Four threads");
        for (int i = 0; i < 7; i++) {
            System.out.println(four_threads_times[i] / 1000000);
        }
        System.out.println("Eight threads");
        for (int i = 0; i < 7; i++) {
            System.out.println(eight_threads_times[i] / 1000000);
        }
    }

    public static boolean check_correct(int[] correct, int[] answer){
        for (int i = 0; i < correct.length; i++) {
            if(correct[i] != answer[i]){
                return false;
            }
        }
        return true;
    }
    
    public static int[] reverse_array(int[] array){
        int count = 0;
        int[] reversed = new int[array.length];
        for(int i = array.length - 1; i >= 0 ; i--){ 
            reversed[count] = array[i];
            count ++;
        }
        return reversed;
    }

}
