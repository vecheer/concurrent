package L2_Atomic;

import org.springframework.util.StopWatch;

import java.util.concurrent.atomic.AtomicReference;

public class C2_AtomicReference {

    static MySafeInteger num = new MySafeInteger(0);

    public static void main(String[] args) throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                num.add(5);
            }
        });


        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                num.add(-5);
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        watch.stop();
        System.out.println("结果: " + num.getValue() + "  耗时: " + watch.getLastTaskTimeMillis());

    }

    // 自定义一个 线程安全的int
    static class MySafeInteger {

        private AtomicReference<Integer> numReference;

        public MySafeInteger(Integer num) {
            this.numReference = new AtomicReference<>(num);
        }

        // CAS 实现
        public void add(int d) {
            Integer E, N;
            do {
                E = numReference.get();
                N = E + d;
            } while (!numReference.compareAndSet(E, N));
        }

        // 获取值
        public int getValue() {
            return numReference.get() == null ? 0 : numReference.get();
        }
    }

}

