import java.util.concurrent.BrokenBarrierException;
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
        CyclicBarrier thread_barr = new CyclicBarrier(numThreads);
        Thread[] threads = new Thread[numThreads];
        Worker[] workers = new Worker[numThreads];

        int segment_start = 0;
        int segment_length = a.length / numThreads;
        int rest = a.length % numThreads;
        int[] result_bucket = new int[numThreads];

        for (int i = 0; i < numThreads - 1; i++) {
            workers[i] = new Worker(i, a, b, segment_start, segment_length, main_barr, thread_barr, result_bucket);
            threads[i] = new Thread(workers[i]);
            segment_start += segment_length;
        }
        workers[numThreads - 1] = new Worker(numThreads - 1, a, b, segment_start, segment_length + rest, main_barr,
                thread_barr, result_bucket);
        threads[numThreads - 1] = new Thread(workers[numThreads - 1]);

        for (int i = 0; i < numThreads; i++) {
            threads[i].start();
        }
        try {
            main_barr.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int max = 0;
        for (int i = 0; i < result_bucket.length; i++)
            if (result_bucket[i] > max)
                max = result_bucket[i];

        int numBits = 0;
        while (max > 1L << numBits)
            numBits++;
        int numDigits = Math.max(1, numBits / useBits);

        int[] bits = new int[numDigits];
        int bitsPerDigit = numBits / numDigits;
        int bit_rest = numBits % numDigits;
        for (int i = 0; i < bits.length - 1; i++) {
            bits[i] = bitsPerDigit;
        }
        bits[bits.length - 1] = bitsPerDigit + rest;

        // for (Worker w : workers) {}
        int shift = 0;
        int[] temp;
        for (int i = 0; i < bits.length; i++) {
            int[][] all_count = new int[numThreads][];
            int[] sum_count = new int[numDigits];
            for (Worker w : workers) {
                w.set_count(numDigits);
                w.set_all_Count(all_count);
                w.set_sum_count(sum_count);
                w.set_mask_len(bits[i]);
                w.set_shift(shift);
            }
            // wait for threads to compute count-array
            try {   main_barr.await();  } catch (Exception e) {}
            // compute index table
            int sum = 0;
            int[] digitPointers = new int[sum_count.length];
            for (int j = 0; i < sum_count.length; i++) {
                digitPointers[i] = sum;
                sum += sum_count[i];
            }
            // TODO assign threads to move the ints
            int table_start = 0;
            int table_segment_length = digitPointers.length - numThreads;
            int table_segment_rest = digitPointers.length % numThreads;
            for (int j = 0; j < workers.length - 1; j++) {
                workers[j].set_table_params(digitPointers, table_start, table_start + table_segment_length);
                table_start += table_segment_length;
            }
            workers[numThreads - 1].set_table_params(digitPointers, table_start,
                    table_start + table_segment_length + table_segment_rest);

            // Finished assigning threads segments
            try {   main_barr.await();  } catch (Exception e) {}
            shift += bits[i];
            for (Worker w : workers) {
                w.swap_ab();
            }
        }
        for (Worker w : workers) {
            w.set_mask_len(-1);
        }

        return a;
    }

}
