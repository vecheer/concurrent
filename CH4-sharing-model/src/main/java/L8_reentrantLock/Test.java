package L8_reentrantLock;

import lombok.extern.log4j.Log4j;
import utils.Timer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Log4j
public class Test {
    static ReentrantLock lock = new ReentrantLock();
    static Condition c1 = lock.newCondition();


    static boolean stop1;


    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try{
                lock.lock();
                log.info("获取到了锁！");
                while (!stop1){
                    try {
                        log.info("继续睡");
                        c1.await(100, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }finally {
                lock.unlock();
            }

        }, "t1");



        t1.start();


        Timer.SECOND.sleep(4);
        lock.lock();
        c1.signal();
        stop1 = true;
    }
}
