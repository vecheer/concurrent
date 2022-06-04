package L1_thread_running;

import java.util.concurrent.TimeUnit;

/**
 * @author yq
 * @version 1.0
 * @date 2022/4/23 23:19
 */
public class C2_thread_stack {
    public static void main(String[] args) throws InterruptedException {
        Runnable runner = () -> {
            System.out.println(Thread.currentThread().getName() + ": hello");
            func();
        };

        new Thread(runner,"Thread-0").start();
        new Thread(runner,"Thread-1").start();

        TimeUnit.SECONDS.sleep(2);
    }

    public static void func() {
        int x = 10;
        x++;
        System.out.println(x + 1);
    }
}
