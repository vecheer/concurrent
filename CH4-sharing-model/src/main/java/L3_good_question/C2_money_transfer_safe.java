package L3_good_question;

import java.util.Random;

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/11 4:22
 */
public class C2_money_transfer_safe {

    static Random random = new Random();
    public static int randomMoney(){
        return random.nextInt(10);
    }

    public static void main(String[] args) throws InterruptedException {
        AmountSafe Jack = new AmountSafe(1000);
        AmountSafe Tommy = new AmountSafe(1000);

        Object lock = new Object();

        Thread jackThread = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                //synchronized (lock){
                    Jack.giveMoney(Tommy, randomMoney());
                //}
            }
        }, "jack-thread");

        Thread tommyThread = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                //synchronized (lock) {
                    Tommy.giveMoney(Jack, randomMoney());
                //}
            }
        }, "tommy-thread");

        jackThread.start();
        tommyThread.start();

        jackThread.join();
        tommyThread.join();

        System.out.println("Jack余额: " + Jack.getMoney());
        System.out.println("Tommy余额: " + Tommy.getMoney());
        System.out.println("二者余额之和 " + (Tommy.getMoney()+Jack.getMoney()));
    }
}

class AmountSafe {
    // 账户当前余额
    private int money;

    private static final Object lock = new Object();

    public AmountSafe(int money) {
        this.money = money;
    }

    public int getMoney() {

        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    // 转账函数
    public void giveMoney(AmountSafe acceptor, int money) {
        // 这样性能不好，因为可能会影响其他所有Amount账户，只要其中存在两个账户正在互转，其他想转账的双方都转不了了
        // 建议还是在main函数中，专门只对某两个线程进行同步即可
        synchronized (lock/* 这里写 AmountSafe.class 也是同理的*/){
            if (money <= this.money) {
                this.money = this.getMoney() - money;
                acceptor.setMoney(acceptor.getMoney() + money);
            }
        }

    }

}