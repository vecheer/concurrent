package L8_reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

public class C5_FairLock {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock(true);
    }
}
