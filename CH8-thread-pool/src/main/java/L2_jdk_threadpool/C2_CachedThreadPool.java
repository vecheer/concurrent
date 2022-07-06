package L2_jdk_threadpool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import utils.time.Timer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

@Log4j
public class C2_CachedThreadPool {
    public static void main(String[] args) {
        /*ExecutorService pool = Executors.newCachedThreadPool();
        pool.execute(()->{
            System.out.println("hello");
        });*/


        SynchronousQueue<User> userSyncQueue = new SynchronousQueue<>();
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    Timer.sleep(3000);
                    userSyncQueue.put(new User("YQ-" + i));
                    log.info("already put user");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t-Putter").start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                Timer.sleep(4000);
                log.info(userSyncQueue.poll());
            }
        },"t-Getter").start();
    }

    @Data
    @AllArgsConstructor
    static class User {
        String name;
    }
}
