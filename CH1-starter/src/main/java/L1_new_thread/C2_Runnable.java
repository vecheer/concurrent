package L1_new_thread;

/**
 * @author yq
 * @version 1.0
 * @date 2022/4/20 22:16
 */
public class C2_Runnable {
    public static void main(String[] args) {
        Runnable runnable = () -> System.out.println("this is my runnable");


        Thread thread = new Thread(runnable);
        thread.setName("my-runnable-1");
        thread.start();
    }
}
