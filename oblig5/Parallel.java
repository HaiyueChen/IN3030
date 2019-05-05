import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Parallel
 */
public class Parallel {

    
    public static IntList get_enfolding(int[] x, int[] y, int num_threads){
        if (num_threads < 4) {
            num_threads = 4;
        }
        RekWorker.count.set(0);
        IntList enfolding = new IntList((int)Math.sqrt(x.length));
        Thread[] threads = new Thread[num_threads];
        Worker[] workers = new Worker[num_threads];
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
            workers[i] = new Worker(i, barr, x, y, section_start, section_end, buckets, indices, dist_buckets);
            threads[i] = new Thread(workers[i]);
            threads[i].start();
            section_start = section_end;
        }
        workers[num_threads - 1] = new Worker(num_threads - 1, barr, x, y, section_start, x.length, buckets, indices, dist_buckets);
        threads[num_threads - 1] = new Thread(workers[num_threads - 1]);
        threads[num_threads - 1].start();
        

        //Phase 1: find index of largest and lowest x-value
        int high_x = x[0];
        int low_x = high_x;
        int index_high_x = 0;
        int index_low_x = 0;
        IntList above_line = new IntList(x.length / 2);
        IntList under_line = new IntList(x.length / 2);
        try {   barr.await();   } catch (Exception e) {}
        for (int i = 0; i < num_threads; i++) {

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
        // System.out.printf("Index high-x: %d val: %d \t Index low-x: %d val: %d\n", index_high_x, high_x, index_low_x, low_x);
        indices[0] = index_high_x;
        indices[1] = index_low_x;
        try {   barr.await();   } catch (Exception e) {}
        //Initiate rek workers
        //The problem can be divided in 4 quadrants from this point, therefore I will allways make 4 threads,
        //each dealing with one of the quadrants.
        Thread[] rek_threads = new Thread[4];
        RekWorker[] rek_workers = new RekWorker[4];
        AtomicInteger free_threads = new AtomicInteger(num_threads - 4);
        for (int i = 0; i < 4; i++) {
            rek_workers[i] = new RekWorker(x, y, free_threads);
            rek_threads[i] = new Thread(rek_workers[i]);
        }

        // Phase 2: Find the upper and lower points
        try {   barr.await();   } catch (Exception e) {}
        double min_dist_right = 0;
        int min_dist_right_index = 0;
        double max_dist_left = 0;
        int max_dist_left_index = 0;
        for (int i = 0; i < dist_buckets.length; i++) {
            above_line.append(workers[i].above_line);
            under_line.append(workers[i].under_line);
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

        //rek_worker 0: top right
        rek_workers[0].index_line_start = index_high_x;
        rek_workers[0].index_line_end = min_dist_right_index;
        rek_workers[0].where_to_search = above_line;

        //rek_worker 1: top left
        rek_workers[1].index_line_start = min_dist_right_index;
        rek_workers[1].index_line_end = index_low_x;
        rek_workers[1].where_to_search = above_line;

        //rek_worker 2: bottom left
        rek_workers[2].index_line_start = index_low_x;
        rek_workers[2].index_line_end = max_dist_left_index;
        rek_workers[2].where_to_search = under_line;

        //rek_worker 3: bottom right
        rek_workers[3].index_line_start = max_dist_left_index;
        rek_workers[3].index_line_end = index_high_x;
        rek_workers[3].where_to_search = under_line;

        for (int i = 0; i < 4; i++) {
            rek_threads[i].start();
        }
        enfolding.add(index_high_x);

        for (int i = 0; i < 4; i++) {
            try {   rek_threads[i].join();  } catch (Exception e) {}
        }
        
        enfolding.append(rek_workers[0].results);
        enfolding.add(min_dist_right_index);

        enfolding.append(rek_workers[1].results);
        enfolding.add(index_low_x);

        enfolding.append(rek_workers[2].results);
        enfolding.add(max_dist_left_index);

        enfolding.append(rek_workers[3].results);
        enfolding.add(index_high_x);

        return enfolding;
    }


}