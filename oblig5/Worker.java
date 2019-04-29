/**
 * Worker
 */
public class Worker implements Runnable{
    
    public int id;
    
    public Worker(int id){
        this.id = id;
    }

    @Override
    public void run(){
        System.out.printf("This is thread: %d\n", id);



    }


}