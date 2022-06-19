package L4_java_markword;

import com.sun.istack.internal.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.openjdk.jol.info.ClassData;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/11 12:37
 */
@Data
class Lock{
    String usage = "for test";

    public Lock(String usage) {
        this.usage = usage;
    }

    public Lock() {
        this.usage = usage;
    }
}

public class C1_markword {
    public static void main(String[] args) {
        Lock lock = new Lock("测试加锁");
        lock.hashCode();

//        System.out.println("[未加锁之前的 lock 对象布局: ] ");
        System.out.println("[初始状态下, 对象内存布局: ] ");
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());

        sleep(3000);

        System.out.println("[3秒后, 对象内存布局: ] ");
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());

        new Thread(()->{
            synchronized (lock){
                System.out.println("------------------------------------------------------");
//                System.out.println("[t1 get the lock, Lock Obj is like: ]");
                System.out.println(ClassLayout.parseInstance(lock).toPrintable());
                sleep(1000);
            }
        },"t1").start();

        sleep(1);

        new Thread(()->{
            synchronized (lock){
                sleep(100);
                System.out.println("------------------------------------------------------");
                System.out.println("[t2 get the lock, Lock Obj is like: ]");
                System.out.println(ClassLayout.parseInstance(lock).toPrintable());
            }
        },"t2").start();


    }


    public static void sleep(int millis){
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
