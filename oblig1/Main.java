import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        int n = Integer.valueOf(args[1]);
        int k = Integer.valueOf(args[2]);


        int[] a = new int[n];
        Random rand = new Random(7361);
        for (int i = 0; i < a.length; i++) {
            a[i] = rand.nextInt(999999999);
        }

        int[] b = a.clone();
        Arrays.sort(b);
        b = reverse_array(b);
        int[] sorted_correct = new int[10];
        for (int i = 0; i < 10; i++) {
            sorted_correct[i] = b[i];
        }
        long seq_start = System.nanoTime();
        int[] sorted = Sequential.find_k_largest(a, 10);
        System.out.println("Sequential time");
        
        sorted = Parallel.find_k_largest(a, 10);
        //sorted = new int[10];
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