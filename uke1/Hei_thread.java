/**
 * Hei_thread
 */
public class Hei_thread implements Runnable{
    private int id;

    public Hei_thread(int ind){
        this.id = ind;
    }

    public void run(){
        System.out.println("Traad nr: " + this.id + " sier hei");
        try {
            Thread.sleep(1000);
            System.out.println("Thread nr: " + this.id + " sier hei etter aa ha ventet et sekund");

        } catch (InterruptedException e) {
            //TODO: handle exception
        }

    }
}
