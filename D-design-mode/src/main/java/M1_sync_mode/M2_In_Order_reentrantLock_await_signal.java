package M1_sync_mode;


import lombok.extern.log4j.Log4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Log4j
public class M2_In_Order_reentrantLock_await_signal {

    static final ReentrantLock lock = new ReentrantLock();
    static final Condition couldPrintA = lock.newCondition();
    static final Condition couldPrintB = lock.newCondition();

    static int lastPrint = 1; // 1A 2B

    public static void main(String[] args) {

        new Thread(()->{
            while(true){
                try {
                    lock.lock();
                    if (lastPrint==2){
                        System.out.print("a");
                        lastPrint=1;
                        couldPrintB.signalAll();
                    }else {
                        couldPrintA.await();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }

            }
        },"t1").start();


        new Thread(()->{
            while(true){
                try {
                    lock.lock();
                    if (lastPrint==1){
                        System.out.print("b");
                        lastPrint=2;
                        couldPrintA.signalAll();
                    }else {
                        couldPrintB.await();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        },"t2").start();

        new Thread(()->{
            while(true){
                try {
                    lock.lock();
                    if (lastPrint==1){
                        System.out.print("b");
                        lastPrint=2;
                        couldPrintA.signalAll();
                    }else {
                        couldPrintB.await();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        },"t3").start();
    }
}
