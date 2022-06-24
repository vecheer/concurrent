package L6_wait_notify;


import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import utils.Timer;

/**
 * notify 是随机唤醒，有虚假唤醒的风险
 * notifyAll 是全部唤醒
 *
 *
 * 下面演示虚假唤醒
 */
@Log4j
public class C7_wait_notify_test {
    static final Object food = new Object();
    static boolean Jack_Food_hamburgers = false;
    static boolean Tommy_Food_rice = false;

    public static void main(String[] args) {



        new Thread(()->{
            log.info("我是Jack，我点的是Jack_Food_hamburgers");
            log.info("我是Jack，我现在在等我的外卖到");
            synchronized (food){
                try {
                    food.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Jack_Food_hamburgers)
                    log.info("我是Jack，外卖到啦！下去拿外卖！");
                else
                    log.error("我是Jack，我的外卖没到，谁把我喊过来拿外卖的？");

            }
        },"Jack").start();

        Timer.sleep(100);

        new Thread(()->{
            log.info("我是Tommy，我点的是Tommy_Food_rice");
            log.info("我是Tommy，我现在在等我的外卖到");
            synchronized (food){
                try {
                    food.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Tommy_Food_rice)
                    log.info("我是Tommy，外卖到啦！下去拿外卖！");
                else
                    log.error("我是Tommy，我的外卖没到，谁把我喊过来拿外卖的？");

            }
        },"Tommy").start();

        Timer.sleep(100);

        /*new Thread(()->{

            synchronized (food){
                log.info("我是外卖员，来送外卖了，赶紧下来拿饭!");
                log.info("我现在送的是Tommy的米饭 Tommy_Food_rice");
                Tommy_Food_rice = true;
                food.notify();
            }
        },"FoodSeller").start();*/

        new Thread(()->{

            synchronized (food){
                log.info("我是外卖员，来送外卖了，赶紧下来拿饭!");
                log.info("我现在送的是Jack的米饭 Jack_Food_hamburgers");
                Jack_Food_hamburgers = true;
                food.notifyAll();
            }
        },"FoodSeller").start();
    }
}
