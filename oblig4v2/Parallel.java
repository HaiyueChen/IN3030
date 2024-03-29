import java.util.concurrent.CyclicBarrier;

/**
 * Parallel
 */
public class Parallel {

    public static int[] sort(int[] unsorted, int useBits, int numThreads) {
        int[] a = unsorted;
        int[] b = a.clone();
        if (a.length < numThreads)
            numThreads = a.length;

        CyclicBarrier main_barr = new CyclicBarrier(numThreads + 1);
        Thread[] threads = new Thread[numThreads];
        Worker[] workers = new Worker[numThreads];

        int segment_start = 0;
        int segment_length = a.length / numThreads;
        int rest = a.length % numThreads;
        int[] result_bucket = new int[numThreads];

        for (int i = 0; i < numThreads - 1; i++) {
            workers[i] = new Worker(i, a, b, segment_start, segment_length, main_barr, result_bucket);
            threads[i] = new Thread(workers[i]);
            segment_start += segment_length;
        }
        workers[numThreads - 1] = new Worker(numThreads - 1, a, b, segment_start, segment_length + rest, main_barr, result_bucket);
        threads[numThreads - 1] = new Thread(workers[numThreads - 1]);

        for (int i = 0; i < numThreads; i++) {
            threads[i].start();
        }
        try {   main_barr.await();  } catch (Exception e) {e.printStackTrace();}

        int max = 0;
        for (int i = 0; i < result_bucket.length; i++) 
            if (result_bucket[i] > max) max = result_bucket[i]; 
        
        int numBits = 0;
        while(max > 1L << numBits) numBits ++;
        int numDigits = Math.max(1, numBits/useBits);

        int[] bits = new int[numDigits];
        int bitsPerDigit = numBits / numDigits;
        int bit_rest = numBits % numDigits;
        for (int i = 0; i < bits.length - 1; i++) {
            bits[i] = bitsPerDigit;
        }
        bits[bits.length - 1] = bitsPerDigit + bit_rest;

        int shift = 0;
        int[] temp;
        for (int i = 0; i < bits.length; i++) {
            int[][] all_count = new int[numThreads][(1 << bits[i])];
            int[] sum_count = new int[(1 << bits[i])];
            int sum_column_start = 0;
            int sum_column_length = (1 << bits[i]) / numThreads;
            int sum_column_rest = (1 << bits[i]) % numThreads;
            for (int j = 0; j < workers.length - 1; j++) {
                Worker w = workers[j];
                w.set_count((1 << bits[i]));
                w.set_all_count(all_count);
                w.set_sum_count(sum_count);
                w.set_mask_len(bits[i]);
                w.set_shift(shift);
                sum_column_start += sum_column_length;
            }
            workers[workers.length - 1].set_count((1 << bits[i]));
            workers[workers.length - 1].set_all_count(all_count);
            workers[workers.length - 1].set_sum_count(sum_count);
            workers[workers.length - 1].set_mask_len(bits[i]);
            workers[workers.length - 1].set_shift(shift);

            // wait for threads to compute count-array
            try {   main_barr.await();  } catch (Exception e) {}
            try {   main_barr.await();  } catch (Exception e) {}
            // compute index table
            int sum = 0;
            int[][] digitPointers = new int[all_count.length][all_count[0].length];
            for (int k = 0; k < all_count[0].length; k++) {
                for (int j = 0; j < all_count.length; j++) {
                    digitPointers[j][k] = sum;
                    sum += all_count[j][k];
                }
            }
            for (int j = 0; j < digitPointers.length; j++) {
                workers[j].count = digitPointers[j];
            }

            // Finished assigning threads segments
            try {   main_barr.await();  } catch (Exception e) {}
            try {   main_barr.await();  } catch (Exception e) {}
            shift += bits[i];
            temp = a;
            a = b;
            b = temp;
        }
        for (Worker w : workers) {
            w.set_mask_len(-1);
        }
        try {   main_barr.await();  } catch (Exception e) {}

        return a;
    }

}
