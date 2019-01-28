import java.util.Arrays;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        int[] randoms = new int[] {1, 3, 6, 8, 100, 9, 5, 10, 7, 99};
        int[] sorted = Sequential.find_k_largest(randoms, 10);
        System.out.println(Arrays.toString(sorted));
    }

    
    
}