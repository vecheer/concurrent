package L1_my_thread_pool;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class C1_my_thread_pool {
    public static void main(String[] args) {
    }


    ///////////////////////////////////////////////////////////////////////////
    // 阻塞队列： 专门存放线程任务，可以存、取任务
    ///////////////////////////////////////////////////////////////////////////
    static class BlockingQueue<T>{
        /* P1 数据结构 ***************************************************************** */
        // 1. 任务队列
        // 存储任务，是一个双向表，因为任务的执行是FIFO的
        private final Deque<T> taskQueue = new ArrayDeque<>();
        // 容量上限
        private int capacity;

        // 2. 锁
        // 控制线程并发放、取任务
        private final ReentrantLock queueLock = new ReentrantLock();

        // 3.条件变量
        // 为生产者、消费者各定义一个等待条件
        private final Condition fullCondition = queueLock.newCondition();
        private final Condition emptyCondition = queueLock.newCondition();


        public BlockingQueue(int capacity) {
            this.capacity = capacity;
        }

        /* P2 功能 ***************************************************************** */
        // 1.存任务
        public void putTask(T task){
            try{
                queueLock.lock();
                // 阻塞添加，为了防止虚假唤醒，加了while(true)
                while (taskQueue.size()==capacity) {
                    fullCondition.await();
                }
                taskQueue.addLast(task);
                emptyCondition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                queueLock.unlock();
            }
        }

        // 2.取任务
        public T takeTask(){
            try{
                queueLock.lock();
                // 阻塞取，为了防止虚假唤醒，加了while(true)
                while (taskQueue.isEmpty()) {
                    emptyCondition.await();
                }
                fullCondition.signal();
                return taskQueue.removeFirst();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } finally {
                queueLock.unlock();
            }
        }

        // 2.取任务(带有超时限制——版本1 手工计时)
        public T takeTask(long timeout, TimeUnit unit){
            long millis = unit.toMillis(timeout);
            long timeRemain = millis;
            long startedTime;
            try{
                queueLock.lock();
                while (taskQueue.isEmpty()) {
                    // 保证虚假唤醒后，还能按照剩下的时间接着等
                    startedTime = System.currentTimeMillis();
                    if (!emptyCondition.await(timeRemain,TimeUnit.MILLISECONDS)) {
                        return null;
                    }
                    timeRemain = /*剩余时间*/timeRemain - /*本次已等待的时间*/(System.currentTimeMillis() - startedTime);
                }
                fullCondition.signal();
                return taskQueue.removeFirst();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } finally {
                queueLock.unlock();
            }
        }

        // 2.取任务(带有超时限制——版本2 awaitNanos)
        public T takeTaskTimed(long timeout, TimeUnit unit){
            long timeRemain = unit.toNanos(timeout);
            try{
                queueLock.lock();
                while (taskQueue.isEmpty()) {
                    // Condition#awaitNanos 返回的是：提前唤醒后，剩余的等待时间
                    timeRemain = emptyCondition.awaitNanos(timeRemain);
                    if (timeRemain<=0)
                        return null;
                }
                fullCondition.signal();
                return taskQueue.removeFirst();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } finally {
                queueLock.unlock();
            }
        }

        /* P3 内部方法 ***************************************************************** */

    }


    ///////////////////////////////////////////////////////////////////////////
    // 线程池
    ///////////////////////////////////////////////////////////////////////////
    static class ThreadPool{
        /* P1 数据结构 ************************************************** */
        // 1.任务集合
        private BlockingQueue<Runnable> taskQueue;
        // 最多可接受任务数

        // 2.线程集合
        private final HashSet<Worker> workers = new HashSet<>();
        // 核心线程数
        private int coreSize;

        // 3.超时时间
        private long timeout;

        public ThreadPool(int coreSize, long timeout, int maxTaskNum) {
            this.coreSize = coreSize;
            this.timeout = timeout;

            this.taskQueue = new BlockingQueue<>(maxTaskNum);
        }

        /* P1 功能 ************************************************** */
        // 1.任务执行
        public void execute(Runnable task){
            // 并发状态下，对于worker.add();worker.size()调用都是线程不安全的
            synchronized (workers){
                // 工作线程 < 预定线程数(一般根据cpu核心数来定)
                if (workers.size() < coreSize){
                    Worker worker = new Worker(task);
                    workers.add(worker);
                    worker.start();
                }else {
                    taskQueue.putTask(task);
                }
            }
        }

        ////////////////////////////////////
        // 包装后的线程
        ////////////////////////////////////
        static class Worker extends Thread{
            private Runnable task;

            public Worker(Runnable task) {
                this.task = task;
            }

            @Override
            public void run() {
                super.run();
            }
        }

    }

}
