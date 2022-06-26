package L8_reentrantLock;

import lombok.extern.log4j.Log4j;
import utils.Timer;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/26 17:56
 */
@Log4j
public class C1_ReentrantLock {

    static ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) {

        new Thread(()->{
            try{
                reentrantLock.lock();
                log.info("现在获取了可重入锁");

                try{
                    reentrantLock.lock();
                    log.info("现在第二次获取了可重入锁");
                }finally {
                    reentrantLock.unlock();
                }

            }finally {
                Timer.SECOND.sleep(5);
                reentrantLock.unlock();
            }

        },"t1").start();

        new Thread(()->{
            Timer.MILLISECONDS.sleep(15);
            try{
                reentrantLock.lock();
                log.info("现在获取了可重入锁");
                try{
                    reentrantLock.lock();
                    log.info("现在第二次获取了可重入锁");
                }finally {
                    reentrantLock.unlock();
                }

            }finally {
                reentrantLock.unlock();
            }
        },"t2").start();

    }

    // 重入性
    public static void reenter(){
        reentrantLock.lock();
        try{
            log.info("现在获取了可重入锁");
            Timer.sleepForever();
        }finally {
            reentrantLock.unlock();
        }

    }
}
