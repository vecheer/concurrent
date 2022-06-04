package M1_design_mode;

import lombok.extern.log4j.Log4j;

import java.util.concurrent.TimeUnit;

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/3 10:12
 */
@Log4j
public class L1_two_phase_stop {
    public static void main(String[] args) throws InterruptedException {
        Watcher watcher = new Watcher();
        watcher.start();

        TimeUnit.SECONDS.sleep(30);
        watcher.interrupt();
    }
}

@Log4j
class Watcher extends Thread  {

    // 监控时记录的数据
    private static int watchData;


    // 监控操作
    public static void watch(){
        log.info("[运行中...]watching......");
        try {
            watchData ++;
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 优雅停机操作
    public static void cleanWork(){
        log.info("[停机中...] 下面开始料理后事");

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("[停机结束] 完毕! 现在可以停机了!");
    }


    // watcher running
    // 运行监控
    @Override
    public void run() {


        log.info("===================== 监控线程开始运行! =====================");

        while(true){

            // ============== P1 停机判断
            // 收到打断通知，开始优雅停机
            if (Thread.currentThread().isInterrupted()){
                log.info("[运行中...]收到打断通知, 现在开始料理后事");
                cleanWork();
                break;
            }



            // ============== P2 正常监控
            watch();
            // 如果打断了，则直接开始优雅停机
            if (Thread.currentThread().isInterrupted())
                continue;

            // ============== P3 睡眠, 让出CPU, 等待下一次监控
            // 5秒监控一次
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                // sleep状态下收到中断信号
                // 下次循环直接优雅停机
                Thread.currentThread().interrupt();
            }
        }

        log.info("===================== 监控线程运行结束!数据如下 =====================");
        log.info("watchData ===> " + watchData);

    }

}

/**
 * 实际上还有个简化版的流程
 */
/*
while(true){
    // ============== P1 停机判断
    if (Thread.currentThread().isInterrupted()){
        log.info("[运行中...]收到打断通知, 现在开始料理后事");
        cleanWork();
        break;
    }

    // ============== P2 睡眠, 让出CPU, 等待下一次监控
    try {
        TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }

    // ============== P3 正常监控
    watch();
}

 */