package L4_java_markword;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/19 1:01
 */
public class C3_biased_and_hashCode {
    public static void main(String[] args) {

        Object lock = new Object();

        System.out.println("[默认加了偏向锁，对象的布局:] ");
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());

        synchronized (lock){
            System.out.println("[线程中获取锁对象时，对象的布局:] ");
            System.out.println(ClassLayout.parseInstance(lock).toPrintable());

            lock.hashCode();

            System.out.println("[获取对象的hashCode后，对象的布局:] ");
            System.out.println(ClassLayout.parseInstance(lock).toPrintable());
        }


    }
}
