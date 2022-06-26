package L1_java_thread_state;

import utils.Timer;

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/26 17:08
 */
public class C3_active_lock {

    static volatile int count = 10;

    public static void main(String[] args) {

        new Thread(() -> {
            // 期望减到 0 退出循环
            while (count > 0) {
                Timer.MILLISECONDS.sleep(100);
                count --;
            }
        }).start();

        new Thread(() -> {
            // 期望超过 20 退出循环
            while (count < 20) {
                Timer.MILLISECONDS.sleep(100);
                count ++;
            }
        }).start();
    }
}
