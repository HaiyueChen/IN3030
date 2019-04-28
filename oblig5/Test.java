import java.util.Arrays;

/**
 * Test
 */
public class Test {

    static int num_point = 10;
    public static void main(String[] args) {
        Oblig5 test = new Oblig5(num_point);
        IntList list = new IntList(num_point);
        // System.out.println(Arrays.toString(test.x));
        list.add(1);
        list.add(1);
        list.add(1);
        TegnUt t = new TegnUt(test, list);
    }
}