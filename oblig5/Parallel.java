import java.util.concurrent.CyclicBarrier;

/**
 * Parallel
 */
public class Parallel {

    
    public static IntList get_enfolding(int[] x, int[] y, int num_threads){
        if (num_threads < 4) {
            num_threads = 4;
        }
        IntList enfolding = new IntList((int)Math.sqrt(x.length));
        Thread[] threads = new Thread[num_threads];
        CyclicBarrier barr = new CyclicBarrier(num_threads + 1);

        int section_start = 0;
        int section_length = x.length / num_threads;
        int rest = x.length % num_threads;

        // [thread_id][high-x-val, high-x-index, low-x-val, low-x-index]
        int[][] buckets = new int[num_threads][4];
        // [high-x-index, low-x-index]
        int[] indices = new int[2];
        // [thread_id][min_dist_right, min_dist_right_index, max_dist_left, max_dist_right_index]
        double[][] dist_buckets = new double[num_threads][4];
        for (int i = 0; i < num_threads - 1; i++) {
            int section_end = section_start + section_length;
            threads[i] = new Thread(new Worker(i, barr, x, y, section_start, section_end, buckets, indices, dist_buckets));
            threads[i].start();
            section_start = section_end;
        }
        threads[num_threads - 1] = new Thread(new Worker(num_threads - 1, barr, x, y, section_start, x.length, buckets, indices, dist_buckets));
        threads[num_threads - 1].start();
        
        //Phase 1: find index of largest and lowest x-value
        int high_x = x[0];
        int low_x = high_x;
        int index_high_x = 0;
        int index_low_x = 0;
        try {   barr.await();   } catch (Exception e) {}
        for (int i = 0; i < buckets.length; i++) {
            int[] new_bucket = buckets[i];
            if (new_bucket[0] > high_x) {
                high_x = new_bucket[0];
                index_high_x = new_bucket[1];
            }
            if (new_bucket[2] < low_x) {
                low_x = new_bucket[2];
                index_low_x = new_bucket[3];
            }
        }
        System.out.printf("Index high-x: %d val: %d \t Index low-x: %d val: %d\n", index_high_x, high_x, index_low_x, low_x);
        indices[0] = index_high_x;
        indices[1] = index_low_x;
        try {   barr.await();   } catch (Exception e) {}
        
        // Phase 2: Find the upper and lower points
        try {   barr.await();   } catch (Exception e) {}
        double min_dist_right = 0;
        int min_dist_right_index = 0;
        double max_dist_left = 0;
        int max_dist_left_index = 0;
        for (int i = 0; i < dist_buckets.length; i++) {
            double[] new_dist_bucket = dist_buckets[i];
            if (new_dist_bucket[0] < min_dist_right) {
                min_dist_right = new_dist_bucket[0];
                min_dist_right_index = (int) new_dist_bucket[1];
            }
            if (new_dist_bucket[2] > max_dist_left) {
                max_dist_left = new_dist_bucket[2];
                max_dist_left_index = (int) new_dist_bucket[3];
            }
        }
        System.out.printf("Index upper point: %d dist: %f \t Index lower point: %d dist: %f\n", min_dist_right_index, min_dist_right, max_dist_left_index, max_dist_left);
        

        return enfolding;
    }


}