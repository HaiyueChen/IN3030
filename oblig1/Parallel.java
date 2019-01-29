import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Parallel
 */
public class Parallel {

    public static int[] find_k_largest(int[] a, int k) {
        int processors = Runtime.getRuntime().availableProcessors();
        int segment_start = 0;
        int segment_length = a.length / processors;
        int[][] result_bucket = new int[processors][k];

        Thread[] threads = new Thread[processors];
        for (int i = 0; i < processors; i++) {
            int segment_end = segment_start + segment_length;
            if(i == processors - 1){
                segment_end += a.length % processors;
            }
           // System.out.println(segment_start + "  " + segment_end);
            threads[i] = new Thread(new Worker(i, 
                                              segment_start,
                                              segment_end, 
                                              a, 
                                              result_bucket, 
                                              k));
            segment_start = segment_end;
        }

        for (int i = 0; i < processors; i++) {
            threads[i].start();
        }

        for (int i = 0; i < processors; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {}
        }

        PriorityQueue<Integer> heap = new PriorityQueue<>(Collections.reverseOrder());
        for (int i = 0; i < result_bucket.length; i++) {
            for (int j = 0; j < result_bucket[i].length; j++) {
                heap.add(result_bucket[i][j]);
            }
        }
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = heap.remove();
        }


        return result;
    }
    
}