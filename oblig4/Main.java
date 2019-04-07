import java.util.Arrays;
import java.util.LinkedList;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        
        int[] org = Oblig4Precode.generateArray(10, 10);
        // int[] a1 = Arrays.copyOf(org, org.length);
        // int[] a2 = Arrays.copyOf(a1, a1.length);
        
        int[] test = new int[10];
        for (int i = 10; i > 0; i--) {
            test[10 - i] = i;
        }
        System.out.println(Arrays.toString(test));
        System.out.println(Arrays.toString(Parallel.sort(test, 8, 1)));




    }

    public static void test_increasing(int[] a){
        LinkedList<Integer[]> wrongIndices = new LinkedList<>();
        for (int i = 1; i < a.length; i++) {
            if(a[i -1] > a[i]) 
                wrongIndices.add(new Integer[]{i - 1, i});
        }
        if (wrongIndices.size() == 0){
            System.out.println("All correct");
            return;
        } 
        System.out.println("Wrong indices:");
        for (Integer[] pairs : wrongIndices){
            System.out.println(Arrays.toString(pairs));
        }
    }

    public static void findMax(int[] a){
        int max = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] > max) {
                max = a[i];
            }
        }
        System.out.println(max);
    }





}