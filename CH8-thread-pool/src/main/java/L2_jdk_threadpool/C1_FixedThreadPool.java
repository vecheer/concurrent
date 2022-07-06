package L2_jdk_threadpool;

import lombok.extern.log4j.Log4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


@Log4j
public class C1_FixedThreadPool {
    public static void main(String[] args) {
        ExecutorService pool1 = Executors.newFixedThreadPool(3);
        pool1.execute(() -> {
            System.out.println("hello");
        });

        ExecutorService pool = Executors.newFixedThreadPool(3, new ThreadFactory() {
            private AtomicInteger prefixCount = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "YQ-POOL-"+prefixCount.getAndIncrement());
            }
        });
    }
}
