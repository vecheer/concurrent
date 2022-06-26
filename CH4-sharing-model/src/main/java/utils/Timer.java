package utils;

import java.util.concurrent.TimeUnit;

public class Timer {

    public static void sleep(int millis){
        if (millis == -1)
            millis = Integer.MAX_VALUE;
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class SECOND{
        public static void sleep(int duration){
            try {
                TimeUnit.SECONDS.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class MILLISECONDS{
        static void sleep(int duration){
            try {
                TimeUnit.MILLISECONDS.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class NANOSECONDS{
        static void sleep(int duration){
            try {
                TimeUnit.NANOSECONDS.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
