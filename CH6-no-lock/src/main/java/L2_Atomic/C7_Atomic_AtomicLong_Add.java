package L2_Atomic;

import org.springframework.util.StopWatch;

import java.util.concurrent.atomic.AtomicLong;

public class C7_Atomic_AtomicLong_Add {

    static AtomicLong num = new AtomicLong(0);

    public static void main(String[] args) throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 500000000; i++) {
                num.addAndGet(15);
            }
        });


        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 500000000; i++) {
                num.addAndGet(-14);
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        watch.stop();
        System.out.println("AtomicLong: 执行次数 " + 500000000 + "  耗时: " + watch.getLastTaskTimeMillis() + "ms");
    }


}
