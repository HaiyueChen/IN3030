import java.util.Arrays;

/**
 * Main
 */
public class Main {
    
    static int num_punkt = 250000000;
    static int num_runs = 7;
    public static void main(String[] args) {
        // test_seq();
        test_para();
        // run_times();
    }

    public static void test_seq(){
        int test_num = 200;
        NPunkter17 gen_punkt = new NPunkter17(test_num);
        Oblig5 test_seq = new Oblig5(test_num);
        IntList test_list = test_seq.seqMethod();
        TegnUt t = new TegnUt(test_seq, test_list);
    }

    public static void test_para(){
        int test_num = 1000;
        NPunkter17 gen_punkt = new NPunkter17(test_num);
        Oblig5 test_para = new Oblig5(test_num);
        IntList test_list = test_para.paraMethod(Runtime.getRuntime().availableProcessors());
        TegnUt t = new TegnUt(test_para, test_list);
    }

    public static void run_times() {
        NPunkter17 gen_punkt = new NPunkter17(num_punkt);
        int[] x = new int[num_punkt];
        int[] y = new int[num_punkt];
        System.out.println("Generating points");
        gen_punkt.fyllArrayer(x, y);

        double[] seq_times = new double[num_runs];
        double[] para_times = new double[num_runs];
        for (int i = 0; i < num_runs; i++) {
            System.out.println("\nStarting run: " + (i + 1));

            System.out.println("Running sequential");
            long seq_start = System.nanoTime();
            IntList seq_res = Sequential.get_enfolding(x, y);
            double seq_time = (System.nanoTime() - seq_start) / 1000000;
            seq_times[i] = seq_time;
            
            System.out.println("Running parallel");
            long para_start = System.nanoTime();
            IntList para_res = Parallel.get_enfolding(x, y, 8);
            double para_time = (System.nanoTime() - para_start) / 1000000;
            para_times[i] = para_time;             
            System.out.println("Seq == Para: " + seq_res.equals(para_res));
            System.out.println("Threads used: " + RekWorker.count.get());
        }

        Arrays.sort(seq_times);
        Arrays.sort(para_times);
        System.out.println("\nSequential time: " + seq_times[num_runs / 2 + 1]);
        System.out.println("Parallel time: " + para_times[num_runs / 2 + 1]);
        System.out.println("Speed up: " + seq_times[num_runs / 2 + 1] / para_times[num_runs / 2 + 1]);
    }
}