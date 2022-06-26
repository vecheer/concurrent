package L1_java_thread_state;

import utils.Timer;

import java.util.concurrent.locks.LockSupport;

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/26 16:30
 */
public class C2_dead_lock {
    public static void main(String[] args) {

        Object A = new Object();
        Object B = new Object();

        Thread t1 = new Thread(()->{
            synchronized (A){
                Timer.SECOND.sleep(1);
                synchronized (B){
                    Timer.sleepForever();
                }
            }
        },"t1");

        Thread t2 = new Thread(()->{
            synchronized (B){
                Timer.SECOND.sleep(1);
                synchronized (A){
                    Timer.sleepForever();
                }
            }
        },"t2");

        t1.start();
        t2.start();
        LockSupport.park();
    }
}
