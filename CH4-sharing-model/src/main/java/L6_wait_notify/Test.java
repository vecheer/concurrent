package L6_wait_notify;

import utils.Timer;

public class Test {
    public static void main(String[] args) {
        Boolean f = new Boolean(true);

        new Runnable(){
            @Override
            public void run() {
                System.out.println(this);
            }
        };

        new Thread(()->{
            Timer.sleep(3000);
            System.out.println("t1 start");
            System.out.println(f);
        },"t1").start();

        System.out.println("main over");
    }

    public void t(){
        int f =4;
        new Thread(()->{
            Timer.sleep(3000);
            System.out.println("t1 start");
            System.out.println(this);
            System.out.println(f);
        },"t1").start();
    }

}
