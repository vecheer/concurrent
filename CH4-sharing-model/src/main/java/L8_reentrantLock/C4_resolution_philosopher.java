package L8_reentrantLock;

import lombok.extern.log4j.Log4j;
import utils.Timer;

@Log4j
public class C4_resolution_philosopher {
    public static void main(String[] args) {
        // 5支筷子
        Object c1 = new Object();
        Object c2 = new Object();
        Object c3 = new Object();
        Object c4 = new Object();
        Object c5 = new Object();

        // 5位哲学家，直接拿筷子
        Philosopher 孟子 = new Philosopher("孟子", c1, c2);
        Philosopher 庄子 = new Philosopher("庄子", c2, c3);
        Philosopher 老子 = new Philosopher("老子", c3, c4);
        Philosopher 坤子 = new Philosopher("坤子", c4, c5);
//        Philosopher 啥子 = new Philosopher("啥子", c5, c1);
        Philosopher 啥子 = new Philosopher("啥子", c5, c1);

        孟子.start();
        庄子.start();
        老子.start();
        坤子.start();
        啥子.start();
    }


    @Log4j
    static class Philosopher extends Thread{

        // 左右两边的筷子
        private Object chopRight;
        private Object chopLeft;

        public Philosopher(String name, Object chopRight, Object chopLeft) {
            super(name);
            this.chopRight = chopRight;
            this.chopLeft = chopLeft;
        }

        @Override
        public void run() {
            while (true){
                // 1.拿起筷子
                synchronized (chopLeft){
                    synchronized (chopRight){
                        // 2.吃饭
                        log.info(getName() + " eating now...");
                        Timer.SECOND.sleep(1);
                    }
                }
            }
        }
    }

}
