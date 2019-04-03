import java.util.concurrent.CyclicBarrier;

/**
 * Parallel
 */
public class Parallel {

    public static int[] sort(int[] unsorted, int useBits, int numThreads){
        int[] a = unsorted;
        int[] b = a.clone();
        if (a.length < numThreads) numThreads = a.length;


        CyclicBarrier barr = new CyclicBarrier(numThreads + 1);
        int segment_Length = a.length / numThreads;
        int rest = a.length % numThreads;
        




        return a;
    }

}