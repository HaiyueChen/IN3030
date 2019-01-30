import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
       int processors = Runtime.getRuntime().availableProcessors();
       /*
       for (int i = 0; i < processors; i++) {
            Thread t = new Thread(new Hei_thread(i));
            t.start();
       }
       */
       int[] a = new int[100000];
       for (int i = 0; i < a.length; i++) {
            a[i] = ThreadLocalRandom.current().nextInt(0,  99999);
        }
        long startTime = System.nanoTime();
        System.out.println(findMax_seq(a));
        long endTime = System.nanoTime();
        System.out.println("Sequential time: " + (endTime - startTime ) / 1000);


        startTime = System.nanoTime();
        System.out.println(findMax_para(a, processors));
        endTime = System.nanoTime();
        System.out.println("parallell time: " + (endTime - startTime) / 1000);
    }

    public static int findMax_seq(int[] a){
        int max_value = a[0];
        for (int i = 1; i < a.length; i++) {
            if(a[i] > max_value){
                max_value = a[i];
            }
        }

        return max_value;
    }

    public static int findMax_para(int[] a, int num_threads){
        int max_value = a[0];
        int[] result_bucket = new int[num_threads];
        int segment = a.length / num_threads;
        int lower = 0;
        Thread[] threads = new Thread[num_threads];
        for (int i = 0; i < num_threads; i++) {
            //System.out.println(lower + "  " + lower + segment);
            threads[i] = new Thread(new FindMax_thread(i, a, lower, lower + segment, result_bucket));
            lower += segment;
        }
        
        for (int i = 0; i < num_threads; i++) {
            threads[i].start();
        }

        for (int i = 0; i < num_threads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
            }
        }

        for (int i = 0; i < result_bucket.length; i++) {
            if(result_bucket[i] > max_value){
                max_value = result_bucket[i];
            }
        }
        return max_value;
    }
}