package L1_simple_data_format;

import lombok.extern.log4j.Log4j;
import utils.MyAtomicInteger;
import utils.Timer;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author yq
 * @version 1.0
 * @date 2022/7/3 2:31
 */
@Log4j
public class C1_SimpleDataFormat {

    static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    static MyAtomicInteger countErr = new MyAtomicInteger(0);
    static MyAtomicInteger countSuccess = new MyAtomicInteger(0);

    public static void main(String[] args) {


        for (int i = 0; i < 1000; i++) {
            new Thread(()->{
                try {
                    synchronized (dateFormat){
                        countSuccess.incrementAndGet();
                        log.info(dateFormat.parse("1951-04-21"));
                    }
                } catch (ParseException e) {
                    countErr.incrementAndGet();
                    log.error(e);
                }
            }).start();
        }

        Timer.sleep(5000);
        System.out.println("countSuccess = " + countSuccess.get());
        System.out.println("countErr = " + countErr.get());
    }
}
