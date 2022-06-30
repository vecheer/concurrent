package L2_Atomic;

import org.springframework.util.StopWatch;

import java.util.concurrent.atomic.AtomicInteger;

public class C1_AtomicInteger_solve_sync {
    static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {


        StopWatch watch = new StopWatch();
        watch.start();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                count.addAndGet(5);
            }
        });


        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                count.addAndGet(-5);
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        watch.stop();
        System.out.println("结果: " + count + "  耗时: " + watch.getLastTaskTimeMillis());

    }


}