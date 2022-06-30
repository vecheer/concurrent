package L2_Atomic;

import utils.Timer;

import java.util.Arrays;

/**
 * @author yq
 * @version 1.0
 * @date 2022/7/1 3:38
 */
public class C5_concurrent_array_operation {

    static int[] array = new int[10];

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Timer.sleep(5);
                array[i] = array[i] + 5;
            }
        },"t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Timer.sleep(5);
                array[i] = array[i] - 5;
            }
        },"t2");

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Timer.sleep(5);
                array[i] = array[i] + 5;
            }
        },"t3");

        Thread t4 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Timer.sleep(5);
                array[i] = array[i] - 5;
            }
        },"t4");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        Arrays.stream(array).forEach(one -> System.out.print(one+","));
    }


}
