<<<<<<< HEAD
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        
        int[] org = Oblig4Precode.generateArray(100000, 10);
        // int[] a1 = Arrays.copyOf(org, org.length);
        // int[] a2 = Arrays.copyOf(a1, a1.length);
        Parallel.sort(org, 8, 8);
        findMax(org);




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





=======
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        
        // for (int i = 0; i < 8; i++) {
        //     System.out.printf("N = %d\n", (int) Math.pow(10,  i + 1));
        //     int[] org = Oblig4Precode.generateArray((int) Math.pow(10,  i + 1), 10);
        //     System.out.println("parasort");
        //     int[] para_res = Parallel.sort(org, 8, 8);
        //     System.out.println("seq sort");
        //     int[] seq_res = Sequential.sort(org, 8);
        //     System.out.println(Arrays.equals(seq_res, para_res));
        //     System.gc();
        // }
        double[] para_times = new double[7];
        double[] seq_times = new double[7];
        
        int[] org = Oblig4Precode.generateArray(100000000, 10);
        System.out.println("Seq start");
        for (int i = 0; i < 7; i++) {
            long seq_time_start = System.nanoTime();
            int[] seq_res = Sequential.sort(org, 12);
            seq_times[i] = (double) (System.nanoTime() - seq_time_start) / 1000000;
            System.gc();
        }
        System.out.println("para start");
        for (int i = 0; i < 7; i++) {
            long para_time_start = System.nanoTime();
            int[] para_res = Parallel.sort(org, 12, 4);
            para_times[i] = (double) (System.nanoTime() - para_time_start) / 1000000;   
            System.gc();
        }

        Arrays.sort(para_times);
        Arrays.sort(seq_times);
        System.out.println(seq_times[4] / para_times[4]);


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





>>>>>>> 9b4fab90babca6fb4e50af39cbb632e9fac2b562
}