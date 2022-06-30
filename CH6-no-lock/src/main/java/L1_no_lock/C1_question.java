package L1_no_lock;

import org.springframework.util.StopWatch;

public class C1_question {
    //static AtomicInteger count = new AtomicInteger(0);


    static int count;

    public static void main(String[] args) throws InterruptedException {


        StopWatch watch = new StopWatch();
        watch.start();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                count = count+5;
            }
        });


        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                count = count+5;
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
