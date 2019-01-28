import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        int[] a = new int[100000];
        for (int i = 0; i < a.length; i++) {
            a[i] = ThreadLocalRandom.current().nextInt(0,  99999);
        }

        int[] b = a.clone();
        Arrays.sort(b);
        int[] sorted_corrct = new int[10];
        for (int i = b.length - 1; i > b.length - 11; i--) {
            sorted_corrct[i]
        }
        int[] sorted = Sequential.find_k_largest(a, 10);
        //int[] sorted = Parallel.find_k_largest(a, 10);
        System.out.println(Arrays.toString(sorted));
    }

    public static void check_correct(int[] correct, int[] answer){
        for (int i = 0; i < correct.length; i++) {
            if(correct[i] != answer[i]){
                System.out.println("Not correct");
                return;
            }
        }
        System.out.println("correct");
    }
    
}