package L1_my_thread_pool;

import com.sun.istack.internal.NotNull;
import lombok.extern.log4j.Log4j;
import utils.time.Timer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


@Log4j
public class C1_my_thread_pool_purify {
    public static void main(String[] args) {
        MyThreadPool threadPool = new MyThreadPool(3,2,8);
        for (int i = 0; i < 20; i++) {
            //Timer.SECOND.sleep(1);
            threadPool.execute(()->{
                log.info("正在执行 biz 任务");
                Timer.SECOND.sleep(1);
            });
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // 阻塞队列： 专门存放线程任务，可以存、取任务
    ///////////////////////////////////////////////////////////////////////////
    static class BlockingQueue<T> {
        // 1. 任务队列
        private final Deque<T> taskQueue = new ArrayDeque<>();
        // 容量上限
        private int capacity;

        // 2. 锁
        private final ReentrantLock queueLock = new ReentrantLock();

        // 3.条件变量
        private final Condition fullCondition = queueLock.newCondition();
        private final Condition emptyCondition = queueLock.newCondition();


        public BlockingQueue(int capacity) {
            this.capacity = capacity;
        }


        // 1.存任务(当任务队列满时，一定时间内存入失败，则放弃存入任务)
        public boolean putTaskTimed(T task,long timeout, TimeUnit unit) {
            long nanos = unit.toNanos(timeout);
            try {
                queueLock.lock();
                // 阻塞添加，为了防止虚假唤醒，加了while(true)
                while (taskQueue.size() == capacity) {
                    log.info("当前任务池已满! 正在等待核心线程执行完毕，来取走任务!");
                    nanos = fullCondition.awaitNanos(nanos);
                    // 一定时间内未等到，则直接放入任务失败
                    if (nanos<=0)
                        return false;
                }
                taskQueue.addLast(task);
                emptyCondition.signal();
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            } finally {
                queueLock.unlock();
            }
        }

        // 2.取任务(带有超时限制 awaitNanos)
        public T takeTaskTimed(long timeout, TimeUnit unit) {
            long timeRemain = unit.toNanos(timeout);
            try {
                queueLock.lock();
                while (taskQueue.isEmpty()) {
                    // Condition#awaitNanos 返回的是：提前唤醒后，剩余的等待时间
                    timeRemain = emptyCondition.awaitNanos(timeRemain);
                    //
                    if (timeRemain <= 0) {
                        log.info("在规定时间内里没有获取到新任务! 不取了!");
                        return null;
                    }
                }

                T t = taskQueue.removeFirst();
                log.info("已取走任务[" + t + "]");

                fullCondition.signal();
                return t;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } finally {
                queueLock.unlock();
            }
        }


    }


    ///////////////////////////////////////////////////////////////////////////
    // 线程池
    // 线程池在功能上，就是不停的从任务队列中取任务来执行，如果当前所有的线程都在运行
    ///////////////////////////////////////////////////////////////////////////

    static class MyThreadPool {
        // 1.任务队列
        private BlockingQueue<Runnable> taskQueue;

        // 2.工作线程集合
        private final HashSet<Worker> workers = new HashSet<>();

        // 3.工作线程数目
        private int coreSize;

        // 4.空闲线程最大存活时间
        private long timeout;

        // 5.任务最大等待时间(************  ***********)
        private long maxWaitingTime;

        // 工作线程数、超时时间、最大任务数、任务最大等待时间
        public MyThreadPool(int coreSize, long timeout, int maxTaskNum) {
            this.coreSize = coreSize;
            this.timeout = timeout;
            //this.maxWaitingTime = maxWaitingTime;

            this.taskQueue = new BlockingQueue<>(maxTaskNum);
        }




        // 执行任务
        public void execute(Runnable task) {

            synchronized (workers) {
                if (workers.size() < coreSize) {
                    Worker worker = new Worker(task);
                    workers.add(worker);
                    worker.start();
                } else {
                    boolean canExecute = taskQueue.putTaskTimed(task, maxWaitingTime, TimeUnit.MILLISECONDS);
                    if (!canExecute) {
                        rejectStrategy();
                    }
                }
            }
        }






        public void rejectStrategy(){

        }

        ////////////////////////////////////
        // 包装后的线程
        ////////////////////////////////////
        class Worker extends Thread {
            private Runnable task;

            public Worker(@NotNull Runnable task) {
                this.task = task;
            }

            public Worker(@NotNull Runnable task,String threadName) {
                this.task = task;
                this.setName(threadName);
            }

            @Override
            public void run() {

                while (task != null) {
                    // 作用类似于全局异常捕获
                    try {
                        // 新建Worker的时候，传入的 task 还未执行，此处直接执行
                        task.run();
                    } catch (Throwable e) {
                        log.error(e);
                    } finally {
                        // 无论是执行完毕，还是出现异常，本次任务算是执行结束了，下面继续去任务队列中取出任务执行
                        task = taskQueue.takeTaskTimed(timeout,TimeUnit.SECONDS);
                    }
                }

                // 当没有任务可以执行(可能是①被中断②达到timeout)
                // 本线程自动消亡(运行结束了)
                log.info("已没有任务，现在态空闲了!线程自动关闭!");
                workers.remove(this);
            }
        }

    }

}
