package L1_volatile;

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/29 21:06
 */
public class C3_happens_before {

             static int a;
    volatile static int b;

    public static void main(String[] args) {





        new Thread(()->{
            a = 5;
            b = 3;
        }).start();

        new Thread(()->{
            System.out.println(b);
            System.out.println(a);
        }).start();
    }
}
