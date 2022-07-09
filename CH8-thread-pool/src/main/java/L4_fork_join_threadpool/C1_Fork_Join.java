package L4_fork_join_threadpool;

import javafx.concurrent.Task;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

// 以计算1到n的和为例
public class C1_Fork_Join {

    public static void main(String[] args) {

        ForkJoinPool pool = new ForkJoinPool();
        Integer sum = pool.invoke(new MyTask(5));

        System.out.println(sum);
    }


    // 自定义任务
    static class MyTask extends RecursiveTask<Integer> {
        private int n;
        public MyTask(int n) {
            this.n = n;
        }

        @Override
        protected Integer compute() {
            // 终止条件
            if (n == 1) {
                return 1;
            }

            MyTask t1 =  new MyTask(n-1);
            t1.fork();  // fork任务拆分：提交本任务【给一个线程执行】
            Integer result = t1.join();// join结果梳理：获取本任务的【执行结果】

            return (n+result);
        }
    }
}
