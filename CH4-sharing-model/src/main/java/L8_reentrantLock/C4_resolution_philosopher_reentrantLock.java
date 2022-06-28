package L8_reentrantLock;

import lombok.extern.log4j.Log4j;
import utils.Timer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Log4j
public class C4_resolution_philosopher_reentrantLock {
    public static void main(String[] args) {
        // 5支筷子
        ReentrantLock c1 = new ReentrantLock(true); // 公平锁
        ReentrantLock c2 = new ReentrantLock(true);
        ReentrantLock c3 = new ReentrantLock(true);
        ReentrantLock c4 = new ReentrantLock(true);
        ReentrantLock c5 = new ReentrantLock(true);

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
                // 没获取到则直接退出
                if (!chopLeft.tryLock()){
                    continue;
                }

                // 2.获取第二根筷子
                try{
                    if (!chopRight.tryLock()){
                        continue;
                    }
                    try{
                        log.info(getName() + " eating now...");
                        Timer.SECOND.sleep(1);
                    }finally {
                        chopRight.unlock();
                    }
                } finally {
                    chopLeft.unlock();
                }

            }
        }
    }
}

