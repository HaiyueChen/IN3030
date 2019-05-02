/**
 * Main
 */
public class Main {
    
    static int num_punkt = 200;
    public static void main(String[] args) {
        NPunkter17 gen_punkt = new NPunkter17(num_punkt);
        int[] x = new int[num_punkt];
        int[] y = new int[num_punkt];
        gen_punkt.fyllArrayer(x, y);
        Sequential.get_enfolding(x, y);
        Parallel.get_enfolding(x, y, 8);
    }
}