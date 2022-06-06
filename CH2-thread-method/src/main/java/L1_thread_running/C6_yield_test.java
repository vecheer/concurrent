package L1_thread_running;

/**
 * @author yq
 * @version 1.0
 * @date 2022/5/22 17:33
 */
public class C6_yield_test {
    public static void main(String[] args) {
        Runnable task1 = () -> {
            int count = 0;
            try {
                for (;;){
                    System.out.println(Thread.currentThread().getName() + ": " + count++);
                }
            }finally {
                System.out.println("t1 count: " + count);

            }
        };

        Runnable task2 = () -> {
            int count = 0;
            try{
                for (;;){
                    //Thread.yield();
                    System.out.println(Thread.currentThread().getName() + ": " + count++);
                }
            }finally {
                System.out.println("t2 count: " + count);
            }

        };

        Thread t1 = new Thread(task1, "t1");
        Thread t2 = new Thread(task2, "t2");

        t1.setPriority(10);
        t2.setPriority(1);
        t1.start();
        t2.start();

    }
}
