package L3_exception_handle_in_threadpool;

import lombok.extern.log4j.Log4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Log4j
public class C2_check_exception_in_future {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService pool = Executors.newFixedThreadPool(3);

        Future<String> future = pool.submit(() -> {
            log.info("doing...");
            int a = 1 / 0;
            return "ok";
        });

        log.info("结果是" + future.get());
    }
}
