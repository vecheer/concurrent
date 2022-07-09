package L3_exception_handle_in_threadpool;

import lombok.extern.log4j.Log4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j
public class C1_try_catch_inside_task {
    public static void main(String[] args)  {

        ExecutorService pool = Executors.newFixedThreadPool(3);

        pool.execute(()->{
            try{
                log.info("doing...");
                int a = 1/0;
            }catch (Exception e){
                log.error(e);
            }
        });
    }
}
