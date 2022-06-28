package M_design_mode;


import lombok.extern.log4j.Log4j;
import utils.Timer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

@Log4j
public class M3_In_Order_park_unpark {

    static final ReentrantLock lock = new ReentrantLock();
    static int lastPrint = 1; // 1A 2B

    static Thread t1,t2;

    public static void main(String[] args) {

        t1 = new Thread(()->{
            while(true){
                if (lastPrint==2){
                    System.out.print("a");
                    lastPrint=1;
                    LockSupport.unpark(t2);
                }
                else{
                    LockSupport.park();
                }
            }
        },"t1");


        t2 = new Thread(()->{
            while(true){
                if (lastPrint==1){
                    System.out.print("b");
                    lastPrint=2;
                    LockSupport.unpark(t1);
                }
                else{
                    LockSupport.park();
                }
            }
        },"t2");


        Timer.sleep(100);

        t1.start();
        t2.start();

    }
}
