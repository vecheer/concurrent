package L2_thread_running;

import lombok.extern.log4j.Log4j;

import java.util.concurrent.TimeUnit;

/**
 * @author yq
 * @version 1.0
 * @date 2022/5/22 19:38
 */
@Log4j
public class C7_join {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                log.info("t1 down!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });



        t1.start();
        t1.join(5_000);

        log.info("main down!");
    }
}
