import java.util.concurrent.CyclicBarrier;

/**
 * Worker
 */
public class Worker implements Runnable{

    public int id;
    public int segment_start;
    public int segment_length;
    public CyclicBarrier barr;

    public Worker(int id, int segment_start, int segment_length, CyclicBarrier barr){
        this.id = id;
        this.segment_start = segment_start;
        this.segment_length = segment_length;
        this.barr = barr;
    }

    @Override
    public void run(){

    }
    
}