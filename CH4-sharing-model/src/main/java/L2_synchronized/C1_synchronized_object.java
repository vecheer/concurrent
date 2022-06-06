package L2_synchronized;

class Lock {
}

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/5 21:36
 */
public class C1_synchronized_object {

    static int count = 0;
    static final Lock lock = new Lock();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (lock){
                    count++;
                }
            }
        });


        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (lock){
                    count--;
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(count);
    }
}
