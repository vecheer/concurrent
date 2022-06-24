package utils;

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
}
