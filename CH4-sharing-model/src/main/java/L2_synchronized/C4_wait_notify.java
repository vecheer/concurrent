package L2_synchronized;


public class C4_wait_notify {

    static Object lock = new Object();

    void func1() {
        synchronized(this){
            doWork();

            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void func2() {
        synchronized(this){
            doWork();
            lock.notify();
            doWork();
        }
    }



    public static void main(String[] args) {

    }



    public static void doWork(){

    }
}
