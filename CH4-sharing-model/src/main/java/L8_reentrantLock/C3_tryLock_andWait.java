package L8_reentrantLock;

import lombok.extern.log4j.Log4j;
import utils.Timer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/27 0:03
 */
@Log4j
public class C3_tryLock_andWait {
    static ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {

            try {
                if (reentrantLock.tryLock(5,TimeUnit.SECONDS))
                    log.info("没超过2秒，并且已经获取到锁!");
                else {
                    log.info("现在获取不到锁!等了5秒都没获取到锁!直接不等了!");
                    return;
                }
            } catch (InterruptedException e) {
                log.info("被人打断了!不等了!");
                return;
            }
            try {
                // 业务代码
                log.info("已获取锁！");
                Timer.SECOND.sleep(1);
            } finally {
                log.info("已获取锁，现在来释放锁");
                reentrantLock.unlock();
            }

        }, "t1");

        reentrantLock.lock();
        t1.start();

        Timer.SECOND.sleep(2);

        t1.interrupt();
        reentrantLock.unlock();


    }
}
