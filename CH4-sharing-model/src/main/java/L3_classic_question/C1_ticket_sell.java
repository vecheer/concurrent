package L3_classic_question;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * ### 卖票问题根源
 * 某个线程准备买票，当它查询当前剩余票数还有票时，就进行接下来的扣票数操作
 * 但实际上，它查询到的结果，已经不是一个准确的结果了
 *
 *
 * @author yq
 * @version 1.0
 * @date 2022/6/11 1:20
 */
@Slf4j
public class C1_ticket_sell {

    // 最终售出票数
    static volatile int soldInAll = 0;
    // 中途保存所有线程购买的票数
    static CopyOnWriteArrayList<Integer> eachNumList = new CopyOnWriteArrayList<>();
    // 方便生成随机数
    static Random random = new Random();


    public static void main(String[] args) throws InterruptedException {
        // 售票窗口
        TicketWindow ticketWindow = new TicketWindow(1000);

        // 第1次for循环 启动所有购票者线程
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(() -> {
                ticketWindow.prepare(); // 准备
                int boughtNum = ticketWindow.sell(randomNum());// 购买
                eachNumList.add(boughtNum);
            });
            threads.add(thread);
            thread.start();
        }

        // 第2次for循环 开启主线程对所有线程的等待
        // 注意！不能在第1个for中join，那样就会退化成串行！！！
        for (Thread thread : threads) {
            thread.join();
        }


        int soldInAll = eachNumList.stream().mapToInt(one->one).sum();
        System.out.println("卖出票数: " + soldInAll);
        System.out.println("剩余票数: " + ticketWindow.getCount());
    }


    public static int randomNum(){
        return random.nextInt(5)+1;
    }
}


/**
 * 售票窗口类
 */
@Slf4j
class TicketWindow{
    private int count;


    public TicketWindow(int count) {
        this.count = count;
    }

    // 获取当前票数
    public int getCount(){
        return count;
    }

    // 卖票  每次可以卖出num张票
    public int sell(int nums){
        if (getCount() >= nums){
            this.count -= nums;
            return nums;
        }
        else {
            log.error("当前票已经售空! 当前票数" + getCount());
            return 0;
        }
    }

    // 处理购买
    public void prepare(){
        try {
            TimeUnit.MILLISECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}