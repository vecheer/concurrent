package M_design_mode;


import lombok.extern.log4j.Log4j;
import utils.Timer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

@Log4j
public class M4_by_turns_park_unpark_abstract {



    public static void main(String[] args) {

        Thread t1, t2, t3, t4;
        Controller threadsController = new Controller();



        t1 = new Thread(() -> {
            threadsController.putInController(3,Thread.currentThread());
            while (true) {
                threadsController.printInTurn("a",3,1);
            }
        }, "t1");


        t2 = new Thread(() -> {
            threadsController.putInController(1,Thread.currentThread());
            while (true) {
                threadsController.printInTurn("b",1,2);
            }
        }, "t2");

        t3 = new Thread(() -> {
            threadsController.putInController(2,Thread.currentThread());
            while (true) {
                threadsController.printInTurn("c",2,3);
            }
        }, "t3");

        t4 = new Thread(() -> {
            threadsController.putInController(2,Thread.currentThread());
            while (true) {
                threadsController.printInTurn("c",2,3);
            }
        }, "t4");



        Timer.sleep(100);

        t1.start();
        t2.start();
        t3.start();

    }

    // 线程控制器 —— 控制线程交错打印
    static class Controller {

        private final ReentrantLock lock = new ReentrantLock();
        private volatile int lastPrint = 1; // 1A 2B 3C

        // 该map存放，需要被控制器控制输出的线程
        // FIXME 将 Map<Integer, Thread> 改成 Map<Integer, Set<Thread>>
        private final Map<Integer, Thread> threadsMap = new ConcurrentHashMap<>();
        // 将线程加入控制器
        public void putInController(int conditionToPrint,Thread thread){
            this.threadsMap.put(conditionToPrint,thread);
        }

        // 控制线程输出
        public void printInTurn(String strToPrint, int conditionForPrint, int printed) {
            if (this.lastPrint == conditionForPrint) {
                System.out.print(strToPrint);
                this. lastPrint = printed;
                LockSupport.unpark(threadsMap.get(printed));
            } else
                LockSupport.park();
        }


    }
}
