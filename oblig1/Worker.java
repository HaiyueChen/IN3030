/**
 * Worker
 */
public class Worker implements Runnable{
    private int id;
    private int segment_start;
    private int segment_end;
    private int[] to_sort;
    private int[][] result_bucket;
    private int k;

    public Worker(  int id, 
                    int segment_start, 
                    int segment_end, 
                    int[] to_sort, 
                    int[][] result_bucket,
                    int k){
        this.id = id;
        this.segment_start = segment_start;
        this.segment_end = segment_end;
        this.to_sort = to_sort;
        this.result_bucket = result_bucket;
        this.k = k;
    }

    
    public void run() {
        int[] result = new int[k];
        for (int i = this.segment_start; i < this.segment_start + k; i++) {
            int key = i;
            int j = i - 1;
            while(j >= this.segment_start && this.to_sort[key] > this.to_sort[j]){
                swap(this.to_sort, key, j);
                key --;
                j --;
            }
        }
        
        for (int i = this.segment_start + k; i < this.segment_end; i++) {
            if(this.to_sort[i] > this.to_sort[this.segment_start + k - 1]){
                swap(this.to_sort, i, this.segment_start + k - 1);
                int key = this.segment_start + k - 1;
                int j = key - 1;
                while(j >= this.segment_start && to_sort[key] > to_sort[j]){
                    swap(to_sort, key, j);
                    key --;
                    j --;                
                }
            }   
        }

        for (int i = 0; i < k; i++) {
            result[i] = to_sort[this.segment_start + i];
        }
        result_bucket[this.id] = result;

    }

    private static final void swap(int[] a, int index_1, int index_2){
        int temp = a[index_1];
        a[index_1] = a[index_2];
        a[index_2] = temp;
    }
    
}