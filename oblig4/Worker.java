import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Worker
 */
public class Worker implements Runnable {

    public int id;
    public int[] a;
    public int segment_start;
    public int segment_length;
    public int[] result_bucket;
    public CyclicBarrier barr;
    public int[] count;
    public int[][] all_count;
    public int[] sum_count;
    public int mask_len;
    public int shift;

    public Worker(int id, int[] a, int segment_start, int segment_length, CyclicBarrier barr, int[] result_bucket) {
        this.id = id;
        this.a = a;
        this.segment_start = segment_start;
        this.segment_length = segment_length;
        this.barr = barr;
        this.result_bucket = result_bucket;
    }

    @Override
    public void run() {
        int max = 0;
        for (int i = segment_start; i < segment_start + segment_length; i++) {
            if (a[i] > max)
                max = a[i];
        }
        this.result_bucket[id] = max;
        try {   barr.await();   } catch (Exception e) {e.printStackTrace();}
        try {   barr.await();   } catch (Exception e) {e.printStackTrace();}
        while (true) {
            if (mask_len == -1) {
                break;
            }
            int mask = (1 << mask_len) - 1;
            for (int i = segment_start; i < segment_start + segment_length; i++) {
                this.count[(a[i] >>> shift) & mask] ++;
            }
            this.all_count[this.id] = this.count;
            try {   barr.await();   } catch (Exception e) {e.printStackTrace();}
            for (int i = 0; i < all_count.length; i++) {
                sum_count[this.id] += all_count[this.id]
            }
        }
    }

    public void set_count(int size){
        this.count = new int[size]; 
    }

    public void set_all_Count(int[][] all_count){
        this.all_count = all_count;
    }

    public void set_sum_count(int[] sum_count){
        this.sum_count = sum_count;
    }

    public void set_mask_len(int mask_len){
        this.mask_len = mask_len;
    }

    public void set_shift(int shift){
        this.shift = shift;
    }

}