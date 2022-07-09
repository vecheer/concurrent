package L2_jdk_threadpool;

import lombok.extern.log4j.Log4j;
import utils.time.Timer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j
public class C7_ScheduledThreadPool {
    public static void main(String[] args) {

        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(1);
        scheduledPool.scheduleWithFixedDelay(()->{
            log.info("test1每隔5s执行");
        },0, 5,TimeUnit.SECONDS);


        /*
        // 普通任务
        scheduledPool.schedule(()->{
            log.info("test1 executing after 4 seconds");
        },4, TimeUnit.SECONDS);

        scheduledPool.schedule(()->{
            log.info("test2 executing after 8 seconds");
        },8, TimeUnit.SECONDS);*/


        /*
        // 周期性任务
        scheduledPool.scheduleAtFixedRate(()->{
            log.info("task定期执行");
            Timer.sleep(5000);
        },0,2,TimeUnit.SECONDS);*/

    }
}
