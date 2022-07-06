package L2_jdk_threadpool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.jetbrains.annotations.NotNull;
import utils.atomic.MyAtomicInteger;
import utils.data.Generator;
import utils.time.Timer;

import java.util.LinkedList;
import java.util.concurrent.*;

/**
 * @author yq
 * @version 1.0
 * @date 2022/7/6 21:07
 */
@Log4j
public class C5_invokeAny_and_Future {
    public static void main(String[] args) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool(new ThreadFactory() {

            private final MyAtomicInteger prefixCounter = new MyAtomicInteger(0);

            @Override
            public Thread newThread(@NotNull Runnable r) {
                return new Thread(r, "putter-" + prefixCounter.incrementAndGet());
            }
        });

        LinkedList<Callable<Time>> taskList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            taskList.add(()->{
                int time = Generator.getRandomInt(15);
                log.info("本线程准备睡眠" + time + "秒");
                Timer.SECOND.sleep(time);
                return new Time(time);
            });
        }

        try {
            Time futureResult = cachedThreadPool.invokeAny(taskList);
            log.info("线程池调用的结果收集是: " + futureResult.getTime());
        } catch (InterruptedException | ExecutionException  e) {
            e.printStackTrace();
        }

    }


    @Data
    @AllArgsConstructor
    static class Time {
        int time;
    }
}
