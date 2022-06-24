package M_design_mode;

import lombok.extern.log4j.Log4j;
import utils.Timer;

@Log4j
public class M1_Protected_Pause_Mode {
    public static void main(String[] args) {
        GuardedObject task = new GuardedObject();

        new Thread(()->{
            log.info("我是消费者，我现在需要等待生产者的结果");
            task.get();
            log.info("我是消费者，我现在已经知道结果了，再见");
        },"consumer").start();

        Timer.sleep(3000);

        new Thread(()->{
            log.info("我是生产者，我现在需要告诉消费者，我的运行结果");
            task.put(new Object());
            log.info("我是生产者，我现在已经把结果交给了同步变量");
        },"producer").start();

    }

    // 作为生产者、消费者线程同步的桥梁
    static class GuardedObject {
        Object result;

        // get结果 供消费者线程使用
        public Object get() {
            // 防止还没put，就过来get了
            synchronized (this) {
                while (result == null) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return result;
            }
        }

        // put结果 供提供者线程使用
        public void put(Object newResult) {
            // 防止上次put的没被消费，就put新的覆盖了
            synchronized (this) {
                while (result != null) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 唤醒消费者来消费
                result = newResult;
                this.notify();
            }
        }

    }
}


