package L2_jdk_threadpool;

import lombok.extern.log4j.Log4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j
public class C3_SingleThreadPool {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newSingleThreadExecutor();

        for (int i = 0; i < 5; i++) {
            int a = i;
            threadPool.execute(()->{
                log.info(a);
                int x = 1/0;
            });
        }
    }
}
