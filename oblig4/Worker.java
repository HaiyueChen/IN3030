import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.Arrays;

/**
 * Worker
 */
public class Worker implements Runnable {

    public int id;
    public int[] a;
    public int[] b;
    public int segment_start;
    public int segment_length;
    public int[] result_bucket;
    public CyclicBarrier main_barr;
    public CyclicBarrier thread_barr;
    public int[] count;
    public int[][] all_count;
    public int[] sum_count;
    public int mask_len;
    public int shift;
    public int sum_column_start;
    public int sum_column_length;
    public int[] table;
    public int table_start;
    public int table_end;

    public Worker(int id, int[] a, int[] b, int segment_start, int segment_length, CyclicBarrier main_barr, CyclicBarrier thread_barr, int[] result_bucket) {
        this.id = id;
        this.a = a;
        this.b = b;
        this.segment_start = segment_start;
        this.segment_length = segment_length;
        this.main_barr = main_barr;
        this.thread_barr = thread_barr;
        this.result_bucket = result_bucket;
    }

    @Override
    public void run() {

        //Find max
        int max = 0;
        for (int i = segment_start; i < segment_start + segment_length; i++) {
            if (a[i] > max)
                max = a[i];
        }
        this.result_bucket[id] = max;
        //Done find max
        try {   main_barr.await();   } catch (Exception e) {e.printStackTrace();}
        //wait for main-thread
        try {   main_barr.await();   } catch (Exception e) {e.printStackTrace();}
        
        while (true) {
            if (mask_len == -1) {
                break;
            }
            //start counting
            int mask = (1 << mask_len) - 1;
            // System.out.printf("Segemnt start: %d  end: %d\n", segment_start, segment_start + segment_length);
            for (int i = segment_start; i < segment_start + segment_length; i++) {
                int index = (a[i] >>> shift) & mask;
                //System.out.println(index);
                //System.out.println(Arrays.toString(this.count));
                this.count[index] ++;
            }
            this.all_count[this.id] = this.count;
            // System.out.println(Arrays.deepToString(this.all_count));
            //wait for other threads to finish counting
            try {   thread_barr.await();   } catch (Exception e) {e.printStackTrace();}
            //summing count for all values of the digit
            for (int i = 0; i < all_count.length; i++) {
                for (int j = sum_column_start; j < sum_column_start + sum_column_length; j++) {
                    sum_count[j] += all_count[i][j];
                }
            }
            System.out.println("sum count from thread " + Arrays.toString(sum_count));
            //wait for other threads to finish summing
            try {   main_barr.await();   } catch (Exception e) {e.printStackTrace();}
            //wait for main thread to compute index table
            try {   main_barr.await();   } catch (Exception e) {e.printStackTrace();}
            
            //swap data between a and b
            for (int i = 0; i < a.length; i++) {
                int pointer_position = (a[i] >>> shift) & mask;
                if (pointer_position >= table_start && pointer_position <= table_end) {
                    b[table[pointer_position]] = a[i];
                    table[pointer_position] ++;
                }
            }
            //wait for other threads to finish swap
            try {   main_barr.await();   } catch (Exception e) {e.printStackTrace();}
            try {   main_barr.await();   } catch (Exception e) {e.printStackTrace();}
        }
    }

    public void set_table_params(int[] table, int start, int end){
        this.table = table;
        this.table_start = start;
        this.table_end = end;
    }

    public void set_count(int size){
        this.count = new int[size]; 
    }

    public void set_all_count(int[][] all_count){
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

    public void swap_ab(){
        int[] temp = a;
        a = b;
        b = temp;
    }

}