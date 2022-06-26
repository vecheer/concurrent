package L8_reentrantLock;

import lombok.extern.log4j.Log4j;
import utils.Timer;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/26 18:54
 */
@Log4j
public class C2_ReentrantLock_interrupt {

    static ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            Timer.MILLISECONDS.sleep(100);
            try {
                log.info("准备竞争锁");
                Timer.SECOND.sleep(1);
                reentrantLock.lock();
                log.info("获取了锁，现在开始无限持有锁，永远不放开");
            } finally {
                Timer.sleepForever();
                reentrantLock.unlock();
            }
        }, "t1");


        Thread t2 = new Thread(() -> {
            log.info("准备竞争锁");
            Timer.SECOND.sleep(3);
            try {
                reentrantLock.lockInterruptibly();
            } catch (InterruptedException e) {
                log.info("main函数打断我了，行！不竞争了，走了!");
                return;
            }
            try{
                log.info("获取到锁喽！开整！");
            } finally {
                reentrantLock.unlock();
            }
        }, "t2");


        t1.start();
        t2.start();


        Timer.SECOND.sleep(5);
        log.info("main函数现在开始打断t2对重入锁的竞争");
        t2.interrupt();
        t1.interrupt();

    }
}
