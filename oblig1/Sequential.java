/**
 * Sequential
 */
public class Sequential {
    public static int[] find_k_largest(int[] a, int k) {
        int[] result = new int[k];
        for (int i = 1; i < k; i++) {
            int key = i;
            int j = i - 1;
            while(j >= 0 && a[key] > a[j]){
                swap(a, key, j);
                key --;
                j --;
            }
        }
        for (int i = k - 1; i < a.length; i++) {
            if(a[i] > a[k - 1]){
                swap(a, i, k - 1);
                int key = k - 1;
                int j = key - 1;
                while(j >= 0 && a[key] > a[j]){
                    swap(a, key, j);
                    key --;
                    j --;                
                }
            }   
        }

        for (int i = 0; i < k; i++) {
            result[i] = a[i];
        }
        return result;
    }

    private static final void swap(int[] a, int index_1, int index_2){
        int temp = a[index_1];
        a[index_1] = a[index_2];
        a[index_2] = temp;
    }
    
}