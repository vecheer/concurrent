package L2_synchronized;

import lombok.Data;

@Data
class LockHolder {
    private int count = 0;

    public void increase() {
        synchronized (this) {
            count++;
        }
    }

    public void decrease() {
        synchronized (this) {
            count--;
        }
    }

    public int getCount() {
        synchronized (this) {
            return count;
        }
    }
}

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/6 0:10
 */
public class C2_synchronized_object_plus {

    static final LockHolder holder = new LockHolder();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                holder.increase();
            }
        });


        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                holder.decrease();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(holder.getCount());
    }
}
