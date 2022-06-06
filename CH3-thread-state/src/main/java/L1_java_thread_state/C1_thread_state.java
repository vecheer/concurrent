package L1_java_thread_state;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

class Lock{

    synchronized void lockIt(){
        try {
            // System.out.println(Thread.currentThread().getName() + "获取了锁");
            TimeUnit.SECONDS.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


/**
 * @author yq
 * @version 1.0
 * @date 2022/6/5 15:59
 */
public class C1_thread_state {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        System.out.println("[新建]的线程状态是: " + t1.getState());

        t1.start();
        TimeUnit.SECONDS.sleep(2);
        System.out.println("[等待IO]的线程状态是: " + t1.getState());


        Thread t2 = new Thread(() -> {
            while(true){
                ;
            }
        },"t2");
        t2.start();
        TimeUnit.SECONDS.sleep(2);
        System.out.println("[正在运行]的线程状态是: " + t2.getState());


        Thread t3 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t3");
        t3.start();
        TimeUnit.SECONDS.sleep(2);
        System.out.println("[Thread.sleep()]的线程状态是: " + t3.getState());

        Lock lock = new Lock();
        Thread tempThread = new Thread(lock::lockIt, "tempThread");
        tempThread.start();

        Thread t4 = new Thread(lock::lockIt,"t4");
        t4.start();
        TimeUnit.SECONDS.sleep(2);
        System.out.println("[等待锁]的线程状态是: " + t4.getState());

        Thread t5 = new Thread(()->{
            try {
                tempThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t5");
        t5.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("[让其他线程join]的线程状态是: " + t5.getState());


        Thread t6 = new Thread(() -> {
            ;
        },"t6");
        t6.start();
        TimeUnit.SECONDS.sleep(2);
        System.out.println("[执行完毕]的线程状态是: " + t6.getState());
    }
}
