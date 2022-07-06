package L2_jdk_threadpool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.jetbrains.annotations.NotNull;
import utils.atomic.MyAtomicInteger;
import utils.data.Generator;
import utils.time.Timer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author yq
 * @version 1.0
 * @date 2022/7/7 0:12
 */
@Log4j
public class C6_shutdown_shutdownNow_awaitTermination {
    public static void main(String[] args) {
        ExecutorService fixedPool = Executors.newFixedThreadPool(3, new ThreadFactory() {

            private final MyAtomicInteger prefixCounter = new MyAtomicInteger(0);

            @Override
            public Thread newThread(@NotNull Runnable r) {
                return new Thread(r, "fixed-pool-" + prefixCounter.incrementAndGet());
            }
        });

        LinkedList<Callable<Time>> taskList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            taskList.add(() -> {
                int time = Generator.getRandomInt(15);
                log.info("本线程准备睡眠" + time + "秒");
                Timer.SECOND.sleep(time);
                log.info("本线程准备睡眠结束!");
                return new Time(time);
            });
        }

        new Thread(() -> {
            try {
                List<Future<Time>> futures = fixedPool.invokeAll(taskList);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();



        new Thread(() -> {
            Timer.SECOND.sleep(5);

            log.info("开始关闭fixedThreadPool!");
                /*
                 List<Runnable> tasks = fixedPool.shutdownNow();
                 log.info("已经关闭线程池，队列中有" + tasks.size() + "个线程未执行");
                */
            List<Runnable> tasks = fixedPool.shutdownNow();
            try {
                boolean over = fixedPool.awaitTermination(5, TimeUnit.SECONDS);
                if (over)
                    log.info("已经关闭线程池!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
            /*cachedThreadPool.execute(()->{
                log.info("现在尝试添加新任务!");
            });*/
            /*log.info("线程池调用的结果收集如下: ");
            futures.forEach(one -> {
                try {
                    System.out.print(one.get().getTime() + " ");
                } catch (InterruptedException | ExecutionException ignored) {
                }
            });*/

    }


    @Data
    @AllArgsConstructor
    static class Time {
        int time;
    }
}
