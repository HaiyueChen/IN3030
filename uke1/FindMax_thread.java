/**
 * FindMax_thread
 */
public class FindMax_thread implements Runnable{
    private int id;
    private int[] to_count;
    private int lower;
    private int upper;
    private int[] result_bucket;
    
    public FindMax_thread(  int id, 
                            int[] to_count, 
                            int lower, 
                            int upper, 
                            int[] result_bucket){
        this.id = id;
        this.to_count = to_count;
        this.lower = lower;
        this.upper = upper;
        this.result_bucket = result_bucket;
    }

    public void run(){
        int max_value = this.to_count[this.lower];
        for (int i = this.lower + 1; i < this.upper; i++) {
            if(this.to_count[i] > max_value){
                max_value = this.to_count[i];
            }
        }
        this.result_bucket[this.id] = max_value;
    }
}