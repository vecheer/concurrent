package L1_thread_running;

import lombok.extern.log4j.Log4j;

import java.util.concurrent.TimeUnit;

/**
 * @author yq
 * @version 1.0
 * @date 2022/5/3 14:36
 */
@Log4j
public class C5_sleep_vs_yield {
    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            log.info("[" + Thread.currentThread().getName() + "] is running!");
            try {
                TimeUnit.SECONDS.sleep(120);
            } catch (InterruptedException e) {
                log.info("now thread was interrupted!!!");
                e.printStackTrace();
            }

            log.info("[" + Thread.currentThread().getName() + "] state is [" + Thread.currentThread().getState() + "]");
        };

        Thread t1 = new Thread(task, "t1");
        log.info("t1.getState() = " + t1.getState());
        t1.start();

        log.info("t1.getState() = " + t1.getState());
        TimeUnit.SECONDS.sleep(3);

        log.info("t1.getState() = " + t1.getState());

        t1.interrupt();








        TimeUnit.SECONDS.sleep(120);
    }




}

@Log4j
class YieldTest{
    public static void main(String[] args) {
        Runnable task = () -> {
            log.info("[" + Thread.currentThread().getName() + "] run now!!!");
            log.info("[" + Thread.currentThread().getName() + "] state is [" + Thread.currentThread().getState() + "]");

            int limit = 10_000;  // 10秒
            long startTime = System.currentTimeMillis();
            while (true){
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;

                if (duration > limit)
                    Thread.yield();

                if (duration%1000 == 0)
                    log.info("[" + Thread.currentThread().getName() + "] :D");

            }
        };


    }
}
