package L3_classic_question;

import java.util.Random;

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/11 4:22
 */
public class C2_money_transfer {

    static Random random = new Random();

    public static int randomMoney() {
        return random.nextInt(10);
    }

    public static void main(String[] args) throws InterruptedException {
        Amount Jack = new Amount(1000);
        Amount Tommy = new Amount(1000);


        Thread jackThread = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                Jack.giveMoney(Tommy, randomMoney());
            }
        }, "jack-thread");

        Thread tommyThread = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                Tommy.giveMoney(Jack, randomMoney());
            }
        }, "tommy-thread");

        jackThread.start();
        tommyThread.start();

        jackThread.join();
        tommyThread.join();

        System.out.println("Jack余额: " + Jack.getMoney());
        System.out.println("Tommy余额: " + Tommy.getMoney());
        System.out.println("二者余额之和 " + (Tommy.getMoney() + Jack.getMoney()));
    }
}


class Amount {
    // 账户当前余额
    private int money;

    public Amount(int money) {
        this.money = money;
    }

    public int getMoney() {

        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    // 转账函数
    public void giveMoney(Amount acceptor, int money) {
        if (money <= this.money) {
            this.money = this.getMoney() - money;
            acceptor.setMoney(acceptor.getMoney() + money);
        }
    }

}