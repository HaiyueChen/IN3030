import java.util.Arrays;

/**
 * Test
 */
public class Test {

    public static void main(String[] args) {
        int[] test = new int[] {1, 2, 3, 4, 5};
        System.out.println(Arrays.toString(reverse_array(test)));        
        
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