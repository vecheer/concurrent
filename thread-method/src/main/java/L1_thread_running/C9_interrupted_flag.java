package L1_thread_running;

import lombok.extern.log4j.Log4j;

import java.util.concurrent.TimeUnit;

/**
 * @author yq
 * @version 1.0
 * @date 2022/5/22 23:20
 */
@Log4j
public class C9_interrupted_flag {
    public static void main(String[] args) throws InterruptedException {
        log.info("main线程运行了!");
        Runnable task1 = ()->{
            Thread me = Thread.currentThread();
            for(;;){
                if(me.isInterrupted() == true){
                    log.info("打断标记为true, 本线程t1主动停止!");
                    break;
                }
            }
        };

        Runnable task2 = ()->{
            Thread me = Thread.currentThread();
            LOOP: for(;;){
                try {
                    log.info("t1开始运行");
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    if(me.isInterrupted() == false){
                        log.info("打断标记为false, t1继续运行!");
                        continue LOOP;
                    }
                    log.info("打断标记为true, 本线程t1主动停止!");
                }

            }
        };

        Thread t1 = new Thread(task2, "t1");
        t1.start();
        log.info("t1线程运行了!");

        TimeUnit.SECONDS.sleep(3);
        log.info("现在开始打断t1线程的执行!");
        t1.interrupt();
    }
}
