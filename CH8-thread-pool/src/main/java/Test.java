import lombok.extern.log4j.Log4j;
import utils.time.Timer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Log4j
public class Test {
    static ReentrantLock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    public static void main(String[] args) {
        new Thread(()->{
            lock.lock();
            try {
                Timer.sleep(1000);
                log.info("开始await()");
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
            log.info("被唤醒");
        },"== 1 ==").start();

        new Thread(()->{
            Timer.sleep(10);
            lock.lock();
            try {
                log.info("调用condition.signal()");
                condition.signal();
                log.info("睡了10秒");
                Timer.sleep(10000);
            }finally {
                lock.unlock();
            }
        },"== 2 ==").start();
    }
}
