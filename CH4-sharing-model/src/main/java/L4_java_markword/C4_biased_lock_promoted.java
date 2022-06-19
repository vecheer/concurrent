package L4_java_markword;

import com.vec.concurrent.Timer;
import org.openjdk.jol.info.ClassLayout;

/**
 * 偏向锁在使用过程中，遇到其他对象来竞争，会自动膨胀为轻量级锁
 * @author yq
 * @version 1.0
 * @date 2022/6/19 1:39
 */
public class C4_biased_lock_promoted {
    public static void main(String[] args) {
        Object lock = new Object();

        System.out.println("[初始态，锁对象的布局:] ");
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());

        new Thread(()->{
            synchronized (lock){
                System.out.println("[t1线程竞争锁时，锁对象的布局:] ");
                System.out.println(ClassLayout.parseInstance(lock).toPrintable());
                Timer.sleep(4000);
            }
        },"t1").start();

        Timer.sleep(3000);
        synchronized (lock){
            System.out.println("[main线程首次获取锁时，锁对象的布局:] ");
            System.out.println(ClassLayout.parseInstance(lock).toPrintable());
        }

    }
}
