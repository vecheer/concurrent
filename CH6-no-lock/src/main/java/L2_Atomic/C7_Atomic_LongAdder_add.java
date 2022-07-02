package L2_Atomic;

import org.springframework.util.StopWatch;

import java.util.concurrent.atomic.LongAdder;

public class C7_Atomic_LongAdder_add {
    static LongAdder num = new LongAdder();

    public static void main(String[] args) throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                num.increment();
            }
        });


        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                num.decrement();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        watch.stop();
        System.out.println("LongAdder: 执行次数 " + 500000000 + "  耗时: " + watch.getLastTaskTimeMillis() + "ms");

    }
}
