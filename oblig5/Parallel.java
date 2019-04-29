/**
 * Parallel
 */
public class Parallel {

    
    public static IntList get_enfolding(int[] x, int[] y, int num_threads){
        IntList enfolding = new IntList(Math.sqrt(x.length));
        Runnable[] runnables = new Runnable[num_threads];
        Thread[] threads = new Thread[num_threads];
        for (int i = 0; i < num_threads; i++) {
            runnables[i] = new Worker(i);
            threads[i] = new Thread(runnables[i]);
            threads[i].start();
        }
        return enfolding;
    }


}