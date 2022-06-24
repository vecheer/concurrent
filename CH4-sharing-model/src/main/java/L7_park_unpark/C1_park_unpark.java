package L7_park_unpark;

import java.util.concurrent.locks.LockSupport;

public class C1_park_unpark {
    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            LockSupport.park();
            System.out.println("t1 已经结束 park!");
        } , "t1");
        t1.start();

        Thread t2 = new Thread(()->{
            System.out.println("t0 打算帮 t1 解除park!");
            LockSupport.unpark(t1);
        } ,"t2");
        t2.start();
    }
}
