import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        for (int j = 0; j < 10000; j++) {
            int[] a = new int[999999];
            for (int i = 0; i < a.length; i++) {
                a[i] = ThreadLocalRandom.current().nextInt(0,  999999999);
            }
    
            int[] b = a.clone();
            Arrays.sort(b);
            b = reverse_array(b);
            int[] sorted_correct = new int[10];
            for (int i = 0; i < 10; i++) {
                sorted_correct[i] = b[i];
            }
            
            //System.out.println(Arrays.toString(sorted));
            //System.out.println(Arrays.toString(sorted_correct));
            //int[] sorted = Sequential.find_k_largest(a, 10);
            int[] sorted = Parallel.find_k_largest(a, 10);
            //sorted = new int[10];
            System.out.println(j);
            if(!check_correct(sorted_correct, sorted)){
                System.out.println("wrong  " + j);
                System.out.println(Arrays.toString(sorted));
                System.out.println(Arrays.toString(sorted_correct));
                break;
            }
            
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