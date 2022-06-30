package L2_Atomic;

import org.springframework.util.StopWatch;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 带有版本号的原子引用
 * @author yq
 * @version 1.0
 * @date 2022/7/1 1:53
 */
public class C3_AtomicStampedReference_resolveABA {

    static MySafeIntegerStamped num = new MySafeIntegerStamped(0);

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

    // 自定义一个 线程安全的int 并解决ABA
    static class MySafeIntegerStamped {

        private AtomicStampedReference<Integer> numReference;

        public MySafeIntegerStamped(Integer num) {
            this.numReference = new AtomicStampedReference<>(num,0);
        }

        // CAS 实现
        public void add(int d) {
            Integer E, N;
            int stamp;
            do {
                stamp = numReference.getStamp();
                E = numReference.getReference();
                N = E + d;
            } while (!numReference.compareAndSet(E, N, stamp,stamp+1));
        }

        // 获取值
        public int getValue() {
            return numReference.getReference() == null ? 0 : numReference.getReference();
        }
    }
}
