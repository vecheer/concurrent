package L2_daemon_thread;

import java.util.concurrent.TimeUnit;

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/4 10:18
 */
public class C1_daemon_thread {
    public static void main(String[] args) throws InterruptedException {

        Thread myThread0 = new Thread(() -> {
            int i = 0;
            System.out.println("子线程开始!");
            do {
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (++i != 100);
            System.out.println("子线程结束!");
        }, "my-thread-0");
        myThread0.setDaemon(true);
        myThread0.start();   // 设置为守护线程之后，无论是否执行完都会直接结束

        TimeUnit.MILLISECONDS.sleep(200);
        System.out.println("main" + "exec over!");
    }
}
