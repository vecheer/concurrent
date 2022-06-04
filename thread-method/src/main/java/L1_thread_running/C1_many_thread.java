package L1_thread_running;

import java.util.concurrent.TimeUnit;

/**
 * @author yq
 * @version 1.0
 * @date 2022/4/22 22:32
 */
public class C1_many_thread {
    public static void main(String[] args) {

        Runnable runner = new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    System.out.println(Thread.currentThread().getName() + ": hello");
                    try {
                        TimeUnit.MILLISECONDS.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        new Thread(runner,"Thread-0").start();
        new Thread(runner,"Thread-1").start();

    }
}
