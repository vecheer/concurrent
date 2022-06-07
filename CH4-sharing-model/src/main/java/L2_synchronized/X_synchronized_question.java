package L2_synchronized;

import lombok.Data;


@Data
class CountHolder {
    private static int count = 0;

    public  void increase() {
        synchronized (this){
            count++;
        }
    }

    public  static void decrease() {
        synchronized (CountHolder.class){
            count--;
        }
    }

    public static int getCount() {
        return count;
    }
}

public class X_synchronized_question {

    static final CountHolder holder = new CountHolder();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 500000; i++) {
                holder.increase();
            }
        },"t1");


        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 500000; i++) {
                holder.decrease();
            }
        },"t2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(CountHolder.getCount());
    }
}
