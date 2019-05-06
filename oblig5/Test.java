import java.util.Arrays;

/**
 * Test
 */
public class Test {

    static int num_point = 1000;

    public static void main(String[] args) {
        Oblig5 test = new Oblig5(num_point);
        IntList seq_list = test.seqMethod();
        System.out.println(Arrays.toString(seq_list.data) + "\n\n");
        IntList para_list = test.paraMethod(8);
        System.out.println(Arrays.toString(para_list.data) + "\n\n");
    }
}