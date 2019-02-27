/**
 * Parallel
 */
public class Parallel {
    
    public static double[][] transpose(double[][] matrix){
        double[][] trans = new double[matrix.length][matrix.length];
        int cores = Runtime.getRuntime().availableProcessors();
        int threads_to_start = matrix.length > cores ?  cores : matrix.length;

        int section_length = matrix.length / threads_to_start;
        int remainder = matrix.length % threads_to_start;

        Thread[] threads = new Thread[threads_to_start];
        int section_start = 0;
        for (int i = 0; i < threads_to_start; i++) {
            if(i == threads_to_start - 1){
                section_length += remainder;
            }
            threads[i] = new Thread(new Transpose_Worker(i, matrix, trans, section_start, section_length));
            section_start += section_length; 
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            try { 
                threads[i].join();
            } catch (InterruptedException e) {            }
        }

        return trans;
    }

    public static double[][] multiply_normal(double[][] a, double[][] b){
        double[][] product = new double[a.length][a.length];
        int cores = Runtime.getRuntime().availableProcessors();
        int threads_to_start = a.length > cores ?  cores : a.length;

        int section_length = a.length / threads_to_start;
        int remainder = a.length % threads_to_start;

        Thread[] threads = new Thread[threads_to_start];
        int section_start = 0;
        for (int i = 0; i < threads_to_start; i++) {
            if(i == threads_to_start - 1){
                section_length += remainder;
            }
            threads[i] = new Thread(new Multiply_worker(i, "normal", a, b, product, section_start, section_length));
            section_start += section_length; 
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            try { 
                threads[i].join();
            } catch (InterruptedException e) {            }
        }

        return product;
    }

    public static double[][] multiply_a_transposed(double[][] a, double[][] b){
        double[][] product = new double[a.length][a.length];
        double[][] trans_a = transpose(a);

        int cores = Runtime.getRuntime().availableProcessors();
        int threads_to_start = a.length > cores ?  cores : a.length;

        int section_length = a.length / threads_to_start;
        int remainder = a.length % threads_to_start;

        Thread[] threads = new Thread[threads_to_start];
        int section_start = 0;
        for (int i = 0; i < threads_to_start; i++) {
            if(i == threads_to_start - 1){
                section_length += remainder;
            }
            threads[i] = new Thread(new Multiply_worker(i, "trans_a", trans_a, b, product, section_start, section_length));
            section_start += section_length; 
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            try { 
                threads[i].join();
            } catch (InterruptedException e) {            }
        }


        return product;
    }

    public static double[][] multiply_b_transposed(double[][] a, double[][] b){
        double[][] product = new double[a.length][a.length];
        double[][] trans_b = transpose(b);

        int cores = Runtime.getRuntime().availableProcessors();
        int threads_to_start = a.length > cores ?  cores : a.length;

        int section_length = a.length / threads_to_start;
        int remainder = a.length % threads_to_start;

        Thread[] threads = new Thread[threads_to_start];
        int section_start = 0;
        for (int i = 0; i < threads_to_start; i++) {
            if(i == threads_to_start - 1){
                section_length += remainder;
            }
            threads[i] = new Thread(new Multiply_worker(i, "trans_b", a, trans_b, product, section_start, section_length));
            section_start += section_length; 
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            try { 
                threads[i].join();
            } catch (InterruptedException e) {            }
        }


        return product;
    }


}

class Transpose_Worker implements Runnable{
    public int id;
    public double[][] matrix;
    public double[][] destination;
    public int section_start;
    public int section_length;

    Transpose_Worker(int id, double[][] matrix, double[][] destination, int section_start, int section_length){
        this.id = id;
        this.matrix = matrix;
        this.destination = destination;
        this.section_start = section_start;
        this.section_length = section_length;
    }

    @Override
    public void run() {
        for (int i = section_start; i < section_start + section_length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.destination[i][j] = this.matrix[j][i];
            }
        }
    }
}

class Multiply_worker implements Runnable{
    public int id;
    public String type;
    public double[][] a;
    public double[][] b;
    public double[][] destination;
    public int section_start;
    public int section_length;

    Multiply_worker(int id, String type, double[][] a, double[][] b, double[][] destination,
                    int section_start, int section_length){
        this.id = id;
        this.type = type;
        this.a = a;
        this.b = b;
        this.destination = destination;
        this.section_start = section_start;
        this.section_length = section_length;
    }

    @Override
    public void run() {
        if(this.type.equals("normal")){
            for (int i = section_start; i < section_start + section_length; i++) {
                for (int j = 0; j < a.length; j++) {
                    for (int k = 0; k < a.length; k++) {
                        this.destination[i][j] += a[i][k] * b[k][j];
                    }
                }
            }
        }
        else if(this.type.equals("trans_a")){
            for (int i = section_start; i < section_start + section_length; i++) {
                for (int j = 0; j < a.length; j++) {
                    for (int k = 0; k < a.length; k++) {
                        this.destination[i][j] += a[k][i] * b[k][j];
                    }
                }
            }    
    
        }
        else if(this.type.equals("trans_b")){
            for (int i = section_start; i < section_start + section_length; i++) {
                for (int j = 0; j < a.length; j++) {
                    for (int k = 0; k < a.length; k++) {
                        this.destination[i][j] += a[i][k] * b[j][k];
                    }
                }
            } 
    
    
        }
    
    
    }
}