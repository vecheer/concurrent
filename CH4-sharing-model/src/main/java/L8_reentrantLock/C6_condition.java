package L8_reentrantLock;

import lombok.extern.log4j.Log4j;
import utils.Timer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Log4j
public class C6_condition {

    static ReentrantLock lock = new ReentrantLock();
    static Condition c1 = lock.newCondition();
    static Condition c2 = lock.newCondition();


    static boolean stop1;
    static boolean stop2;


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
                Timer.SECOND.sleep(2);
            }finally {
                lock.unlock();
            }

        }, "t1");


        Thread t2 = new Thread(() -> {
            try{
                lock.lock();
                log.info("获取到了锁！");
                while (!stop2){
                    try {
                        log.info("继续睡");
                        c2.await(100, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Timer.SECOND.sleep(2);
            }finally {
                lock.unlock();
            }

        }, "t2");




        t1.start();
        t2.start();


        Timer.SECOND.sleep(9);
        c2.signal();
        stop2 = true;
        c1.signal();
        stop1 = true;
    }
}
