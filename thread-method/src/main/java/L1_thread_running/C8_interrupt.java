package L1_thread_running;

import lombok.extern.log4j.Log4j;

import java.util.concurrent.TimeUnit;

/**
 * @author yq
 * @version 1.0
 * @date 2022/5/22 21:29
 */
@Log4j
public class C8_interrupt {
    public static void main(String[] args) throws InterruptedException {

        log.info("main thread running");

        Runnable task = ()->{
            Thread thisThread = Thread.currentThread();
            try {
                TimeUnit.SECONDS.sleep(50);
            } catch (InterruptedException e) {
                log.warn("now was interrupted");
                log.info("stack trace like: " );
                for (StackTraceElement stackTraceElement : thisThread.getStackTrace()) {
                    log.info(stackTraceElement);
                }
                e.printStackTrace();
            }
        };

        Thread t1 = new Thread(task,"t1");
        t1.start();

        TimeUnit.SECONDS.sleep(5);
        t1.interrupt();




    }
}
