package L1_new_thread;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
class User{
    private String name;
    private int age;
}

/**
 * @author yq
 * @version 1.0
 * @date 2022/4/20 22:47
 */
public class C3_FutureTask {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 构造器参数是一个 Callable 接口
        FutureTask<User> task = new FutureTask<>(()->{
            TimeUnit.SECONDS.sleep(1);
            System.out.println("future task running...");
            TimeUnit.SECONDS.sleep(1);
            return new User("WYQ",20);
        });

        Thread thread = new Thread(task,"my-futureTask-1");
        //thread.start();

        System.out.println(task.get());

    }
}
