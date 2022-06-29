package M1_sync_mode;

import lombok.extern.log4j.Log4j;
import utils.Timer;

@Log4j
public class M1_Protected_Pause_Mode_optimize {
    public static void main(String[] args) {
        GuardedObject_optimize task = new GuardedObject_optimize();

        new Thread(()->{
            log.info("我是消费者，我现在需要等待生产者的结果");
            Object o = task.get(10_000);
            if (o == null)
                log.info("我是消费者，我等不及了，再见!!!");
            else
                log.info("我是消费者，我现在已经知道结果了，再见");
        },"consumer").start();

        new Thread(()->{
            log.info("我是生产者，我现在需要告诉消费者，我的运行结果");
            Timer.sleep(8_000);
            log.info("我是生产者，我现在虚假唤醒一波!");
            task.put(null);
            /*log.info("我是生产者，我现在要真的唤醒了！");
            task.put(new Object());
            log.info("我是生产者，我现在已经把结果交给了同步变量");*/
        },"producer").start();

    }

    // 加了等待超时的保护性暂停
    static class GuardedObject_optimize {
        Object result;

        // get结果 供消费者线程使用
        public Object get(int timeout) {
            long start = System.currentTimeMillis();
            // 防止还没put，就过来get了
            synchronized (this){
                // 没有删除while循环，是防止没到timeout，就被唤醒
                while (result == null) {
                    long passedTime = System.currentTimeMillis() - start;
                    if (passedTime >= timeout)  // 判断是否有必要等下去
                        break;
                    try {
                        // 如果总体上等待的时间还差2ms就超时了，此时应该等待2ms而非timeout
                        this.wait(timeout-passedTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
            return result;
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


