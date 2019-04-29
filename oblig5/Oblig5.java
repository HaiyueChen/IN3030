import java.util.Arrays;

/**
 * Oblig5
 */
public class Oblig5 {

    int[] x;
    int[] y;
    int n;
    int MAX_X;
    int MAX_Y;

    public Oblig5(int n) {
        this.n = n;
        NPunkter17 generator = new NPunkter17(n);
        this.x = new int[n];
        this.y = new int[n];
        generator.fyllArrayer(x, y);
        this.MAX_X = findMax(x);
        this.MAX_Y = findMax(y);
    }

    public IntList seqMethod() {
        return Sequential.get_enfolding(x, y);
    }

    public IntList paraMethod(int num_threads) {
        return Parallel.get_enfolding(x, y, num_threads);
    }

    private int findMax(int[] a) {
        int max = a[0];
        for (int i = 1; i < a.length; i++) {
            if (a[i] > max) {
                max = a[i];
            }
        }
        return max;
    }

}