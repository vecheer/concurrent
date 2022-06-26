package L8_reentrantLock;

import lombok.extern.log4j.Log4j;
import utils.Timer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/26 23:46
 */
@Log4j
public class C3_tryLock {

    static ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {

            try {
                if (reentrantLock.tryLock(2, TimeUnit.SECONDS))
                    log.info("现在立即就能获取锁!");
                else{
                    log.info("现在获取不到锁!");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            try{
                // 业务代码
                log.info("已获取锁！");
                Timer.SECOND.sleep(1);
            }finally {
                log.info("已获取锁，现在来释放锁");
                reentrantLock.unlock();
            }

        }, "t1");

        reentrantLock.lock();
        t1.start();


    }
}
