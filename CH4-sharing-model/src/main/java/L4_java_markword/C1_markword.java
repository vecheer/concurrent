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
@AllArgsConstructor
class Lock{
    String usage;
}

public class C1_markword {
    public static void main(String[] args) {
        Lock lock = new Lock("测试加锁");
        System.out.println("[未加锁之前的 lock 对象布局: ] ");
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());

        new Thread(()->{
            synchronized (lock){
                sleep(10);
                System.out.println("------------------------------------------------------");
                System.out.println("[t1 get the lock, Lock Obj is like: ]");
                System.out.println(ClassLayout.parseInstance(lock).toPrintable());
            }
        },"t1").start();


        new Thread(()->{
            synchronized (lock){
                sleep(10);
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
