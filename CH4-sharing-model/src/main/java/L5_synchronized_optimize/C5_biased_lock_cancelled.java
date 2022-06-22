package L5_synchronized_optimize;

import org.openjdk.jol.info.ClassLayout;
import utils.Timer;

/**
 * 偏向锁在使用过程中，遇到其他对象来竞争，会自动膨胀为轻量级锁
 * @author yq
 * @version 1.0
 * @date 2022/6/19 1:39
 */
public class C5_biased_lock_cancelled {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        //--------------------------------
        System.out.println("[初始态，锁对象的布局:] ");
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());

        Thread t0 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("[t0线程首次获取锁时，锁对象的布局:] ");
                System.out.println(ClassLayout.parseInstance(lock).toPrintable());
            }
            int a = 10;
        }, "t0");
        t0.start();

        //--------------------------------
        t0.join();
        System.out.println("(t0当前状态是: " + t0.getState() + ")");
        System.out.println("[t0运行结束后，锁对象的布局:] ");
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());


        //--------------------------------
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("[t0死亡，t1线程拿到锁时，锁对象的布局:] ");
                System.out.println(ClassLayout.parseInstance(lock).toPrintable());
            }
            int a = 10;
        }, "t1");
        t1.start();


        //--------------------------------
        t1.join();
        System.out.println("[最终锁对象的布局:] ");
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());
    }
}
