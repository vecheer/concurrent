package L1_new_thread;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yq
 * @version 1.0
 * @date 2022/4/14 0:15
 */
@Log4j
public class C1_Thread {
    public static void main(String[] args) {

        Thread thread = new Thread(){
            @Override
            public void run() {
                log.info("this is my Thread");;
            }
        };

        thread.setName("yq-thread-1");
        thread.start();



    }
}
