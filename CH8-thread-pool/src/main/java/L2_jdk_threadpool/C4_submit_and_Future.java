package L2_jdk_threadpool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.jetbrains.annotations.NotNull;
import utils.atomic.MyAtomicInteger;
import utils.time.Timer;

import java.util.concurrent.*;

/**
 * @author yq
 * @version 1.0
 * @date 2022/7/6 20:16
 */
@Log4j
public class C4_submit_and_Future {
    public static void main(String[] args) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool(new ThreadFactory() {

            private final MyAtomicInteger prefixCounter = new MyAtomicInteger(0);

            @Override
            public Thread newThread(@NotNull Runnable r) {
                return new Thread(r,"putter-"+prefixCounter.incrementAndGet());
            }
        });

        for (int i = 0; i < 5; i++) {

            int index = i;

            new Thread(() -> {

                /*Callable<User> task = () -> {
                    Timer.sleep(2000);
                    User u = new User("YQ" + index);
                    log.info("put user!");
                    return u;
                };*/

                Callable<User> task = () -> {
                    log.info("put user!");
                    return new User("YQ" );
                };
                Future<User> result = cachedThreadPool.submit(task);


                try {
                    log.info("result is: " + result.get());
                } catch (InterruptedException | ExecutionException ignored) {
                }
            },"getter-" + index).start();
        }
    }


    @Data
    @AllArgsConstructor
    static class User {
        String name;
    }
}
