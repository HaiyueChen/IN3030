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
        double[] para_times = new double[13];
        double[] seq_times = new double[13];
        
        int[] org = Oblig4Precode.generateArray(100000000, 25);
        System.out.println("Seq start");
        for (int i = 0; i < 13; i++) {
            long seq_time_start = System.nanoTime();
            int[] seq_res = Sequential.sort(org, 8);
            seq_times[i] = (double) (System.nanoTime() - seq_time_start) / 1000000;
            System.gc();
        }
        System.out.println("para start");
        for (int i = 0; i < 13; i++) {
            long para_time_start = System.nanoTime();
            int[] para_res = Parallel.sort(org, 8, 8);
            para_times[i] = (double) (System.nanoTime() - para_time_start) / 1000000;   
            System.gc();
        }

        Arrays.sort(para_times);
        Arrays.sort(seq_times);
        System.out.println(seq_times[7] / para_times[7]);


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