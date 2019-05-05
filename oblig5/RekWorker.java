import java.util.concurrent.atomic.AtomicInteger;

/**
 * RekWorker
 */
public class RekWorker implements Runnable {

    public static AtomicInteger count = new AtomicInteger(0);
    public int id;
    public int[] x;
    public int[] y;
    public int index_line_start;
    public int index_line_end;
    public IntList results = new IntList(100);
    public AtomicInteger free_thread_count;
    public IntList where_to_search;
    
    public RekWorker(int[] x, int[] y, AtomicInteger free_thread_count){
        this.id = count.getAndIncrement();
        this.x = x;
        this.y = y;
        this.free_thread_count = free_thread_count;
        // this.index_line_start = index_line_start;
        // this.index_line_end = index_line_end;
    }

    @Override
    public void run() {
        results = sek_Rek_Right(this.index_line_start, this.index_line_end, this.where_to_search);
    }



    public IntList sek_Rek_Right(int index_line_start, int index_line_end, IntList where_to_search){
        IntList index_list = new IntList(10);
        int min_dist_right_index = 0;
        double min_dist_right = 0;
        boolean found_outer = false;
        IntList to_the_right = new IntList(where_to_search.len / 2);
        for (int i = 0; i < where_to_search.len; i++) {
            int index_to_serach = where_to_search.get(i);
            if (index_to_serach == index_line_start || index_to_serach == index_line_end) {
                continue;
            }
            double new_dist_to_line = dist_to_line(index_line_start, index_line_end, index_to_serach);
            if (new_dist_to_line < 0) {
                to_the_right.add(index_to_serach);
            }
            if (new_dist_to_line < min_dist_right){
                min_dist_right = new_dist_to_line;
                min_dist_right_index = index_to_serach;
                found_outer = true;
            }
        }
        // System.out.println(found_outer);
        if (found_outer) {
            // System.out.println(free_thread_count.get());
            if (free_thread_count.get() > 0) {
                // System.out.println("Starting new thread");
                free_thread_count.getAndDecrement();
                RekWorker child_worker = new RekWorker(this.x, this.y, free_thread_count);
                child_worker.index_line_start = min_dist_right_index;
                child_worker.index_line_end = index_line_end;
                child_worker.where_to_search = to_the_right;
                Thread child_thread = new Thread(child_worker);
                child_thread.start();
                
                IntList my_list = sek_Rek_Right(index_line_start, min_dist_right_index, to_the_right);
                try {   child_thread.join();    } catch (Exception e) {}
                free_thread_count.getAndIncrement();
                index_list.append(my_list);
                index_list.add(min_dist_right_index);
                index_list.append(child_worker.results);
            }
            else{
                IntList children_list_1 = sek_Rek_Right(index_line_start, min_dist_right_index, where_to_search);
                IntList children_list_2 = sek_Rek_Right(min_dist_right_index, index_line_end, where_to_search);
                index_list.append(children_list_1);
                index_list.add(min_dist_right_index);
                index_list.append(children_list_2);
            }
            return index_list;
        }
        return index_list;
    }

    public double dist_to_line(int index_line_start, int index_line_end, int index_new_point){
        int x1 = this.x[index_line_start];
        int y1 = this.y[index_line_start];
        
        int x2 = this.x[index_line_end];
        int y2 = this.y[index_line_end];
        
        int x_new = this.x[index_new_point];
        int y_new = this.y[index_new_point];
        
        int a = y1 - y2;
        int b = x2 - x1;
        int c = y2*x1 - y1*x2;
        
        double dist = (a * x_new + b * y_new + c ) / Math.sqrt(a * a + b * b);
        return dist;
    }

}