package L1_thread_running;

import java.util.concurrent.TimeUnit;

/**
 * @author yq
 * @version 1.0
 * @date 2022/4/24 0:02
 */
public class C3_thread_method {
    public static void main(String[] args) throws InterruptedException {
        Runnable runner = () -> {
            System.out.println(Thread.currentThread().getName() + ": hello");

            long start = System.currentTimeMillis();
            for (;;){
                long now = System.currentTimeMillis();
                if (now - start <10_000)
                    continue;
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread myThread =  new Thread(runner, "Thread-0");

        System.out.println("myThread.getState() = " + myThread.getState());
        myThread.start();
        myThread.start();
        System.out.println("myThread.getState() = " + myThread.getState());
        TimeUnit.SECONDS.sleep(2);
        System.out.println("myThread.getState() = " + myThread.getState());

        System.out.println("myThread.isAlive() = " + myThread.isAlive());
        System.out.println("t2.isAlive() = " + myThread.isAlive());

        System.out.println("t1.isInterrupted() = " + myThread.isInterrupted());
        System.out.println("Thread.currentThread() = " + Thread.currentThread());


    }

}
