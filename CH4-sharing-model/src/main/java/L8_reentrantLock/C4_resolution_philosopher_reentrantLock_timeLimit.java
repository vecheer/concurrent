package L8_reentrantLock;

import lombok.extern.log4j.Log4j;
import utils.Timer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Log4j
public class C4_resolution_philosopher_reentrantLock_timeLimit {
    public static void main(String[] args) {
        // 5支筷子
        ReentrantLock c1 = new ReentrantLock();
        ReentrantLock c2 = new ReentrantLock();
        ReentrantLock c3 = new ReentrantLock();
        ReentrantLock c4 = new ReentrantLock();
        ReentrantLock c5 = new ReentrantLock();

        // 5位哲学家，直接拿筷子
        Philosopher 孟子 = new Philosopher("孟子", c1, c2);
        Philosopher 庄子 = new Philosopher("庄子", c2, c3);
        Philosopher 老子 = new Philosopher("老子", c3, c4);
        Philosopher 坤子 = new Philosopher("坤子", c4, c5);
        Philosopher 啥子 = new Philosopher("啥子", c5, c1);
        // 通过更改获取筷子的顺序来解决问题
        // Philosopher 啥子 = new Philosopher("啥子", c5, c1);

        孟子.start();
        庄子.start();
        老子.start();
        坤子.start();
        啥子.start();
    }


    @Log4j
    static class Philosopher extends Thread {

        // 左右两边的筷子
        private ReentrantLock chopRight;
        private ReentrantLock chopLeft;

        public Philosopher(String name, ReentrantLock chopRight, ReentrantLock chopLeft) {
            super(name);
            this.chopRight = chopRight;
            this.chopLeft = chopLeft;
        }

        @Override
        public void run() {
            while (true) {
                // 1.获取第一根筷子
                try {
                    // 如果100ms内没有拿到筷子，就不等了，这次先不吃了
                    if (!chopLeft.tryLock(100, TimeUnit.MILLISECONDS))
                        continue;
                } catch (InterruptedException ignored) {
                    return;
                }

                // 2.获取第二根筷子
                try{
                    try {
                        // 如果100ms内没有拿到筷子，就不等了，这次先不吃了
                        if (!chopRight.tryLock(100, TimeUnit.MILLISECONDS)){
                            continue;
                        }
                    } catch (InterruptedException ignored) {
                        return;
                    }
                    try{
                        // 吃饭
                        log.info(getName() + " eating now...");
                        Timer.SECOND.sleep(1);
                    } finally {
                        chopRight.unlock();
                    }
                } finally {
                    chopLeft.unlock();
                }

            }
        }
    }
}

