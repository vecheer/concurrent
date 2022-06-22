package L5_synchronized_optimize;


import org.junit.rules.Stopwatch;
import org.springframework.util.StopWatch;

/**
 * JIT会通过逃逸分析，消除掉不必要的同步
 */
public class C7_lock_eliminate {
    public static void main(String[] args) {

        StopWatch timer = new StopWatch();

        // 计时开始---------------------------------
        timer.start();
        for (int i = 0; i < 1000_000; i++) {
            func();
        }
        timer.stop();
        // 计时结束 ---------------------------------

        System.out.println("-XX:+EliminateLocks 调用耗时: " + timer.getLastTaskTimeMillis() + "ms");
    }


    public static void func(){
        Object lock = new Object();
        synchronized (lock){
            int a = 125;
            int b = 5;
            int c = a * b;

            int d = 13;
            int e = 172;
            int f = d * e;
        }
    }
}
