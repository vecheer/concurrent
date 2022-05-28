package L2_thread_running;

import java.util.concurrent.TimeUnit;

/**
 * @author yq
 * @version 1.0
 * @date 2022/5/2 21:31
 */
public class C4_run_vs_start {
    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            System.out.println("[" + Thread.currentThread().getName() + "] is running!");
            System.out.println("[" + Thread.currentThread().getName() + "] state is [" + Thread.currentThread().getState() + "]");
        };

        new Thread(task).run();
        new Thread(task).start();

        TimeUnit.SECONDS.sleep(120);
    }
}
