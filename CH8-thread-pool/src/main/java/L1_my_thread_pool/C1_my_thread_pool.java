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
public class C1_my_thread_pool {
    public static void main(String[] args) {
        /*MyThreadPool threadPool = new MyThreadPool(2, 2, 8,*//*拒绝策略*//*(queue, task) -> {
            log.info("现在执行自定义决绝策略!自定义拒绝策略就是打日志，抛异常!");
            throw new RuntimeException("server is busy! wait for a moment!!!");
        });*/
        MyThreadPool threadPool = new MyThreadPool(2, 2, 8,/*拒绝策略*/(queue, task) -> {
            log.info("现在执行自定义决绝策略!自定义拒绝策略就是先等一段时间。");
            queue.putTaskTimed(task,5000,TimeUnit.MILLISECONDS);
        });

        for (int i = 0; i < 50; i++) {
            //Timer.SECOND.sleep(1);
            threadPool.execute(() -> {
                log.info("正在执行 biz 任务");
                Timer.SECOND.sleep(2);
            });
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // 阻塞队列： 专门存放线程任务，可以存、取任务
    ///////////////////////////////////////////////////////////////////////////
    static class TaskBlockingQueue<T> {
        /* P1 数据结构 ***************************************************************** */
        // 1. 任务队列
        // 存储任务，是一个双向表，因为任务的执行是FIFO的
        private final Deque<T> taskQueue = new ArrayDeque<>();
        // 容量上限
        private int maxTaskNum;

        // 2. 锁
        // 控制线程并发放、取任务
        private final ReentrantLock queueLock = new ReentrantLock();

        // 3.条件变量
        // 为生产者、消费者各定义一个等待条件
        private final Condition fullCondition = queueLock.newCondition();
        private final Condition emptyCondition = queueLock.newCondition();


        public TaskBlockingQueue(int maxTaskNum) {
            this.maxTaskNum = maxTaskNum;
        }

        /* P2 功能 ***************************************************************** */
        // 1.存任务(死等)
        public void putTask(T task) {
            try {
                queueLock.lock();
                // 阻塞添加，为了防止虚假唤醒，加了while(true)
                while (taskQueue.size() == maxTaskNum) {
                    log.info("当前任务池已满! 正在等待核心线程执行完毕，来取走任务!");
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

        // 1.存任务(当任务队列满时，一定时间内存入失败，则放弃存入任务)
        public boolean putTaskTimed(T task, long timeout, TimeUnit unit) {
            long nanos = unit.toNanos(timeout);
            try {
                queueLock.lock();
                // 阻塞添加，为了防止虚假唤醒，加了while(true)
                while (taskQueue.size() == maxTaskNum) {
                    log.info("当前任务池已满! 正在等待核心线程执行完毕，来取走任务!");
                    nanos = fullCondition.awaitNanos(nanos);
                    // 一定时间内未等到，则直接放入任务失败
                    if (nanos <= 0){
                        log.info("已超时，不再放入任务队列");
                        return false;
                    }
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

        // 1.存任务(当任务队列满时，执行拒绝策略)
        public void tryPut(MyThreadPool.RejectPolicy<T> policy, T task) {
            try {
                queueLock.lock();
                if (taskQueue.size() < maxTaskNum) {
                    taskQueue.addLast(task);
                } else {
                    policy.reject(this, task);
                }
            } finally {
                queueLock.unlock();
            }
        }


        // 2.取任务
        public T takeTask() {
            try {
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

 /*       // 2.取任务(带有超时限制——版本1 手工计时)
        public T takeTask(long timeout, TimeUnit unit) {
            long millis = unit.toMillis(timeout);
            long timeRemain = millis;
            long startedTime;
            try {
                queueLock.lock();
                while (taskQueue.isEmpty()) {
                    // 保证虚假唤醒后，还能按照剩下的时间接着等
                    startedTime = System.currentTimeMillis();
                    if (!emptyCondition.await(timeRemain, TimeUnit.MILLISECONDS)) {
                        return null;
                    }
                    timeRemain = *//*剩余时间*//*timeRemain - *//*本次已等待的时间*//*(System.currentTimeMillis() - startedTime);
                }
                fullCondition.signal();
                return taskQueue.removeFirst();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } finally {
                queueLock.unlock();
            }
        }*/

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

        /* P3 内部方法 ***************************************************************** */

    }


    ///////////////////////////////////////////////////////////////////////////
    // 线程池
    // 线程池在功能上，就是不停的从任务队列中取任务来执行，如果当前所有的线程都在运行
    ///////////////////////////////////////////////////////////////////////////
    static class MyThreadPool {
        /* P1 数据结构 ************************************************** */
        // 1.任务队列
        private final TaskBlockingQueue<Runnable> taskQueue;

        // 2.工作线程集合
        private final HashSet<Worker> workers = new HashSet<>();
        // 核心线程数(可以真正同时并发执行的线程, 有超出该数目的任务, 就需要放入阻塞队列)
        // 核心线程数是一直在运行的线程，他们负责从阻塞队列中取出Runnable类型的task，来执行
        private final int coreSize;

        // 3.空闲线程存活时间
        // 单个线程在 timeout 时间内，没有任务，则线程回收
        private final long timeout;

        // 4.拒绝策略
        private final RejectPolicy<Runnable> policy;

        // 工作线程数、超时时间、最大任务数、拒绝策略
        public MyThreadPool(int coreSize, long timeout, int maxTaskNum, RejectPolicy<Runnable> policy) {
            this.coreSize = coreSize;
            this.timeout = timeout;
            this.policy = policy;

            this.taskQueue = new TaskBlockingQueue<>(maxTaskNum);
        }

        /* P1 功能 ************************************************** */
        // 1.任务执行
        // 外部调用该方法，来让线程池执行任务，本质就是将任务(runnable)放入任务池
        // 将任务放入任务池，从任务池中取任务都设置有超时时间
        public void execute(Runnable task) {
            // 并发状态下，对于worker.add();worker.size()调用都是线程不安全的
            synchronized (workers) {
                if (workers.size() < coreSize) {
                    log.info("当前线程池比较空闲!先去创建线程!");
                    Worker worker = new Worker(task);
                    workers.add(worker);
                    worker.start();
                } else {
                    log.info("当前线程池比较繁忙，当前任务已放入阻塞队列");
                    try {
                        // 试着放入任务队列(满了则执行拒绝策略)
                        taskQueue.tryPut(policy, task);
                    } catch (Throwable e) {
                        log.error(e);
                    }

                }
            }
        }

        ////////////////////////////////////
        // 包装后的线程
        ////////////////////////////////////
        class Worker extends Thread {
            private Runnable task;

            public Worker(@NotNull Runnable task) {
                this.task = task;
            }

            public Worker(@NotNull Runnable task, String threadName) {
                this.task = task;
                this.setName(threadName);
            }

            // 任务执行
            // ①当前task未执行完，则执行当前task
            // ②当前task执行结束，
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
                        task = taskQueue.takeTaskTimed(timeout, TimeUnit.SECONDS);
                    }
                }

                // 当没有任务可以执行(可能是①被中断②达到timeout)
                // 本线程自动消亡(运行结束了)
                log.info("已没有任务，现在态空闲了!线程自动关闭!");
                workers.remove(this);
            }

        }


        ////////////////////////////////////
        // 拒绝策略
        // 策略模式
        ////////////////////////////////////
        interface RejectPolicy<T> {
            void reject(TaskBlockingQueue<T> taskQueue, T task);
        }
    }

}
