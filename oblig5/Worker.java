import java.util.concurrent.CyclicBarrier;

/**
 * Worker
 */
public class Worker implements Runnable {

    public int id;
    public CyclicBarrier barr;
    public int[] x;
    public int[] y;
    public int section_start;
    public int section_end;

    public int[][] buckets;
    public int[] indices;
    public double[][] dist_buckets;

    public IntList above_line;
    public IntList under_line;

    public Worker(int id, CyclicBarrier barr, int[] x, int[] y, int section_start, int section_end,
            int[][] buckets, int[] indices, double[][] dist_buckets) {
        this.id = id;
        this.barr = barr;
        this.x = x;
        this.y = y;
        this.section_start = section_start;
        this.section_end = section_end;
        this.buckets = buckets;
        this.indices = indices;
        this.dist_buckets = dist_buckets;
        this.above_line = new IntList((section_end - section_start) / 2);
        this.under_line = new IntList((section_end - section_start) / 2);
    }

    @Override
    public void run() {
        // System.out.printf("This is thread: %d\n", id);
        int high_x = x[section_start];
        int index_high_x = section_start;
        int low_x = high_x;
        int index_low_x = section_start;
        for (int i = section_start + 1; i < section_end; i++) {
            int curr = x[i];
            if (curr > high_x) {
                high_x = curr;
                index_high_x = i;
            }
            if (curr < low_x) {
                low_x = curr;
                index_low_x = i;
            }
        }
        buckets[id][0] = high_x;
        buckets[id][1] = index_high_x;
        buckets[id][2] = low_x;
        buckets[id][3] = index_low_x;

        try {   barr.await();   } catch (Exception e) {}
        try {   barr.await();   } catch (Exception e) {}
        int min_dist_right_index = 0;
        int max_dist_left_index = 0;
        double min_dist_right = dist_to_line(index_high_x, index_low_x, min_dist_right_index);
        double max_dist_left = min_dist_right;
        for (int i = section_start; i < section_end; i++) {
            double new_dist_to_line = dist_to_line(indices[0], indices[1], i);
            if (new_dist_to_line < 0) {
                above_line.add(i);
            }
            if (new_dist_to_line > 0) {
                under_line.add(i);
            }
            if (new_dist_to_line < min_dist_right) {
                min_dist_right = new_dist_to_line;
                min_dist_right_index = i;
            }
            if (new_dist_to_line > max_dist_left) {
                max_dist_left = new_dist_to_line;
                max_dist_left_index = i;
            }
        }
        dist_buckets[id][0] = min_dist_right;
        dist_buckets[id][1] = min_dist_right_index;
        dist_buckets[id][2] = max_dist_left;
        dist_buckets[id][3] = max_dist_left_index;
        try {   barr.await();   } catch (Exception e) {}
    }

    public double dist_to_line(int index_line_start, int index_line_end, int index_new_point){
        int x1 = this.x[index_line_start];
        int y1 = this.y[index_line_start];
        
        int x2 = this.x[index_line_end];
        int y2 = this.y[index_line_end];
        
        int x_new = this.x[index_new_point];
        int y_new = this.y[index_new_point];
        
        int a = y1 - y2;
        int b = x2 - x1;
        int c = y2*x1 - y1*x2;
        
        double dist = (a * x_new + b * y_new + c ) / Math.sqrt(a * a + b * b);
        return dist;
    }

}