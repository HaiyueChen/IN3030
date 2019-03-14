

class SieveWorker implements Runnable{
    public SieveMonitor m;
    public byte[] byteArray;
    public int section_start;
    public int section_length;
    public int value_start;
    public int value_end;

    public SieveWorker(SieveMonitor m,
                       int value_start,
                       int value_length){
        this.m = m;
        m.total_threads ++;
        // m.working_threads ++;
        this.byteArray = m.byteArray;

        this.value_start = value_start;
        this.value_end = value_start + value_length;
        // System.out.println(String.format("%d %d", value_start, value_end));
    }

    @Override
    public void run(){
        m.working_threads ++;
        int old_prime = 0;
        // int new_prime = 3;
        while (true) {
            // m.working_threads ++;
            int new_prime = m.fetch_current_prime(old_prime);
            if(new_prime == 0){
                m.working_threads --;
                m.total_threads --;
                break;
            }
            else if(new_prime >= this.value_end){
                m.working_threads --;
                m.total_threads --;
                break;
            }
            // System.out.println("Travrsing: " + new_prime);
            traverse(new_prime);
            // System.out.println("Done Travrsing: " + new_prime);
            // System.out.println(new_prime);
            old_prime = new_prime;
            // System.out.println("Fetch new prime, old prime: " + new_prime);
            // System.out.println(old_prime);
        }
    }

    private void flip(int i) {
        if (i % 2 == 0) {
            return;
        }

        int byteCell = i / 16;
        int bit = (i / 2) % 8;

        this.byteArray[byteCell] |= (1 << bit);
    }

    private void traverse(int p) {
        // System.out.println(p);
        int rest = this.value_start % p; 
        int start = this.value_start;

        if(start == p){
            start += p * 2;
        }
        else if(rest == 0){
            if(start % 2 == 0){
                start += p;
            }
        }
        else{
            if(start < p){
                start = p;
                start += p * 2;
            }
            else{
                start += (p - rest);
                if(start % 2 == 0){
                    start += p;
                }
            }
        }

        for (int i = start; i < this.value_end; i += p * 2) {
            // System.out.println(i);
            flip(i);
        }


        // System.out.println(String.format("%d %d %d", p, offset, start));
        // if(p == 5){
            // System.out.println(start);
            // for (int i = start; i < this.value_end; i += p * 2) {
                // System.out.println(i);
                // flip(i);
            // }
        // }
        // else{
            // System.out.println(start);
            // for (int i = start; i < this.value_end; i += p * 2) {
                // System.out.println(i);
                // flip(i);
            // }
        // }
    }

}

// class PrimeWorker implements Runnable {
//     public SieveMonitor m;

//     public PrimeWorker(SieveMonitor m){
//         this.m = m;
//     }

//     @Override
//     public void run() {
//         try {
//             m.get_new_prime.await();
            
//         } catch (Exception e) {
//             //TODO: handle exception
//         }
//         while (true) {
//             // try {
//             //     Thread.sleep(20);
                
//             // } catch (Exception e) {
//             //     //TODO: handle exception
//             // }
//             // System.out.println(m.currentPrime);
//             int status_code = m.set_next_prime();
//             if(status_code == 0) break;
//         }
//     }
// }