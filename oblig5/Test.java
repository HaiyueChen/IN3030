import java.util.Arrays;

/**
 * Test
 */
public class Test {

    static int num_point = 30;
    public static void main(String[] args) {
        if (args.length == 1) {
            int count = Integer.valueOf(args[0]);
            Oblig5 test = new Oblig5(count);
            IntList test_list = test.seqMethod();
            TegnUt t = new TegnUt(test, test_list);    
        }
        else{
            Oblig5 test = new Oblig5(num_point);
            IntList test_list = test.seqMethod();
            TegnUt t = new TegnUt(test, test_list);
        }
    }
}