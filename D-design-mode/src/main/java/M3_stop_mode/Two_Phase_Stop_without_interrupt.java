package M3_stop_mode;

import lombok.extern.log4j.Log4j;
import utils.Timer;

import java.util.concurrent.TimeUnit;


/**
 * 不使用interrupt来实现 两阶段终止
 */
@Log4j
public class Two_Phase_Stop_without_interrupt {
    public static void main(String[] args) {
        Watcher watcher = new Watcher();
        watcher.start();
        watcher.start();

        Timer.SECOND.sleep(33);
        watcher.stopWatch();
    }

    @Log4j
    static class Watcher extends Thread  {

        // 监控时记录的数据
        private int watchData;

        // 终止标记
        private volatile boolean stopFlag;
        // 线程终止
        public void stopWatch() {
            this.stopFlag = true;
            // 如果在睡眠，则直接打断，睡眠的catch块代码这里需要进行业务判断，是否停止
            Thread.currentThread().interrupt();
        }
        // 线程打断  PS: 打断也可以停止业务
        public void interrupt() {
            // 打断也可以终止线程
            this.stopFlag = true;
            super.interrupt();
        }

        // 监控操作
        public void watch(){
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

            while(!stopFlag){
                // ============== P1 正常监控
                watch();
                if (stopFlag) // 监控状态下可能会收到停止信号，所以监控一结束就停止
                    break;

                // ============== P2 睡眠, 让出CPU, 等待下一次监控
                // 5秒监控一次
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException ignored) {
                    log.info("睡眠时被打断!");
                    if (stopFlag) // 监控状态下可能会收到停止信号，所以监控一结束就停止
                        break;
                }
            }
            log.info("===================== 监控线程运行结束!数据如下 =====================");
            log.info("watchData ===> " + watchData);
        }

    }
}
