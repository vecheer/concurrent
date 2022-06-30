package L2_Atomic;

import org.springframework.util.StopWatch;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class C4_AtomicMarkableReference_resolveABA {

    static MySafeInteger num = new MySafeInteger(0);

    public static void main(String[] args) throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                num.add(5,true);
            }
        });


        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                num.add(-5,false);
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        watch.stop();
        System.out.println("结果: " + num.getValue() + "  耗时: " + watch.getLastTaskTimeMillis());

    }

    // 自定义一个 线程安全的int 并解决ABA
    static class MySafeInteger {

        private AtomicMarkableReference<Integer> numReference;

        public MySafeInteger(Integer num) {
            this.numReference = new AtomicMarkableReference<>(num,true);
        }

        // 只有在标记为flag的情况下才能CAS
        public void add(int d,boolean flag) {
            Integer E, N;
            do {
                E = numReference.getReference();
                N = E + d;
            } while (!numReference.compareAndSet(E, N, flag,!flag));
        }

        // 获取值
        public int getValue() {
            return numReference.getReference() == null ? 0 : numReference.getReference();
        }
    }

}

