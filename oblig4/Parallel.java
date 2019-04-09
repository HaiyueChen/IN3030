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

        CyclicBarrier barr = new CyclicBarrier(numThreads + 1);
        Thread[] threads = new Thread[numThreads];
        Worker[] workers = new Worker[numThreads];

        int segment_start = 0;
        int segment_length = a.length / numThreads;
        int rest = a.length % numThreads;
        int[] result_bucket = new int[numThreads];
        for (int i = 0; i < numThreads - 1; i++) {
            workers[i] = new Worker(i, a, segment_start, segment_length, barr, result_bucket);
            threads[i] = new Thread(workers[i]);
            segment_start += segment_length;
        }
        workers[numThreads - 1] = new Worker(numThreads - 1, a, segment_start, segment_length + rest, barr, result_bucket); 
        threads[numThreads - 1] = new Thread(workers[numThreads - 1]);

        for (int i = 0; i < numThreads; i++) {
            threads[i].start();
        }
        try {   barr.await();   } catch (Exception e) {e.printStackTrace();}
        
        
        
        int max = 0;
        for (int i = 0; i < result_bucket.length; i++) 
            if (result_bucket[i] > max) max = result_bucket[i]; 
        

        int numBits = 0;
        while(max > 1L << numBits) numBits ++;
        int numDigits = Math.max(1, numBits/useBits);

        int[] bits = new int[numDigits];
        int bitsPerDigit = numBits/numDigits;
        int bit_rest = numBits % numDigits;
        for (int i = 0; i < bits.length - 1; i++) {
            bits[i] = bitsPerDigit;
        }
        bits[bits.length - 1] = bitsPerDigit + rest;


        int shift = 0;
        int[] temp;
        for (int i = 0; i < bits.length; i++) {
            int[][] all_count = new int[numThreads][];
            int[] sum_count = new int[numDigits];
            for (Worker w : workers) {
                w.set_count(numDigits);
                w.set_all_Count(all_count);
                w.set_sum_count(sum_count);
                w.set_mask_len(bit[i]);
                w.set_shift(shift);
            }
            try {   barr.await();   } catch (Exception e) {e.printStackTrace();}
            try {   barr.await();   } catch (Exception e) {e.printStackTrace();}
            try {   barr.await();   } catch (Exception e) {e.printStackTrace();}
            
            shift += bits[i];
            temp = a;
            a = b;
            b = temp;
        }
        for (Worker w : workers) {
            w.set_mask_len(-1);
        }

        return a;
    }

}
