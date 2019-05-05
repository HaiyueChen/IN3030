import java.util.Arrays;

/**
 * Main
 */
public class Main {
    
    static int num_runs = 7;
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("The correct way to run this program:");
            System.out.println("\tjava Main {flag} {n}");
            System.exit(0);
        }
        String flag = args[0];
        int n = Integer.valueOf(args[1]);
        if (flag.equals("test-seq")) {
            test_seq(n);
        }
        else if (flag.equals("test-para")) {
            test_para(n);
        }
        else if (flag.equals("run")) {
            run_times(n);
        }
        else{
            System.out.println("Did not recognize the flag: " + flag);
            System.out.println("Please try again");
            System.exit(0);
        }
    }

    public static void test_seq(int test_num){
        if (test_num > 5000) {
            System.out.println("The GUI is not suitable for n greater than 5000.");
            System.out.println("Running the test with n = 5000");
            test_num = 5000;
        }
        NPunkter17 gen_punkt = new NPunkter17(test_num);
        Oblig5 test_seq = new Oblig5(test_num);
        IntList test_list = test_seq.seqMethod();
        TegnUt t = new TegnUt(test_seq, test_list);
    }

    public static void test_para(int test_num){
        if (test_num > 5000) {
            System.out.println("The GUI is not suitable for n greater than 5000.");
            System.out.println("Running the test with n = 5000");
            test_num = 5000;
        }
        NPunkter17 gen_punkt = new NPunkter17(test_num);
        Oblig5 test_para = new Oblig5(test_num);
        IntList test_list = test_para.paraMethod(Runtime.getRuntime().availableProcessors());
        TegnUt t = new TegnUt(test_para, test_list);
    }

    public static void run_times(int num_punkt) {
        NPunkter17 gen_punkt = new NPunkter17(num_punkt);
        int[] x = new int[num_punkt];
        int[] y = new int[num_punkt];
        System.out.println("Generating points");
        gen_punkt.fyllArrayer(x, y);

        double[] seq_times = new double[num_runs];
        double[] para_times = new double[num_runs];
        
        IntList seq_res = null;
        System.out.println("\nRunning sequential");
        for (int i = 0; i < num_runs; i++) {
            long seq_start = System.nanoTime();
            seq_res = Sequential.get_enfolding(x, y);
            double seq_time = (System.nanoTime() - seq_start) / 1000000;
            seq_times[i] = seq_time;
            System.out.printf("Sequential run: %d\ttime: %f ms\n", i+1, seq_time);    
        }

        System.out.println("\nRunning parallel");
        for (int i = 0; i < num_runs; i++) {
            long para_start = System.nanoTime();
            IntList para_res = Parallel.get_enfolding(x, y, 8);
            double para_time = (System.nanoTime() - para_start) / 1000000;
            para_times[i] = para_time;             
            System.out.printf("Parallel   run: %d\ttime: %f ms\tThreads used: %d\tCorrect: %b\n", i+1, para_time, RekWorker.count.get(), seq_res.equals(para_res));
            
        }

        Arrays.sort(seq_times);
        Arrays.sort(para_times);
        System.out.println("\nSequential time: " + seq_times[num_runs / 2 + 1]);
        System.out.println("Parallel time: " + para_times[num_runs / 2 + 1]);
        System.out.println("Speed up: " + seq_times[num_runs / 2 + 1] / para_times[num_runs / 2 + 1]);
    }
}
