package test;

import org.springframework.util.StopWatch;

public class Test3 {
    public static void main(String[] args) {
        StopWatch watch = new StopWatch();
        watch.start();
        System.out.println(sum(1500));
        watch.stop();
        System.out.println("耗时为: " + watch.getLastTaskTimeNanos() + "纳秒");
    }

    public static int sum(int n){
        if (n==1)
            return 1;
        else
            return sum(n-1) + n;
    }
}
