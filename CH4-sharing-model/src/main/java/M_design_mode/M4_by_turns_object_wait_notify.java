package M_design_mode;


import lombok.extern.log4j.Log4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Log4j
public class M4_by_turns_object_wait_notify {

    static final Object lock = new Object();
    static volatile int lastPrint = 1;  // 记录上次打印的字符  1表示A  2表示B  3表示C


    public static void main(String[] args) {

        new Thread(()->{
            while(true){
                synchronized (lock){
                    if (lastPrint==3){
                        //log.info("a");
                        System.out.print("a");
                        lastPrint = 1;
                        lock.notifyAll();
                    }
                    else {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        },"t1").start();


        new Thread(()->{
            while(true){
                synchronized (lock){
                    if (lastPrint==1){
                        //log.info("b");
                        System.out.print("b");
                        lastPrint = 2;
                        lock.notifyAll();
                    }
                    else {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        },"t2").start();

        new Thread(()->{
            while(true){
                synchronized (lock){
                    if (lastPrint==2){
                        //log.info("b");
                        System.out.print("c");
                        lastPrint = 3;
                        lock.notifyAll();
                    }
                    else {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        },"t3").start();

    }
}
