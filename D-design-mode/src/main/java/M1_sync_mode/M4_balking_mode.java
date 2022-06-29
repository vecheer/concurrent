package M1_sync_mode;

/**
 * balk 犹豫模式
 * 做某件事之前，先判断标记是否已经做过，做过就标记做过
 * 但是多线程情况下，会同时判断和修改做过标记
 */
public class M4_balking_mode {
    public static void main(String[] args) {

    }


    /**
     * 以运行一个线程为例，我们希望该线程start方法只调用一次
     */
    static class Worker extends Thread{

        private volatile boolean started;

        @Override
        public synchronized void start() {
            if (started)
                return;

            started = true;
            super.start();
        }

        // 优化
        public synchronized void start1() {
            if (started)
                return;

            started = true;
            super.start();
        }
    }
}
