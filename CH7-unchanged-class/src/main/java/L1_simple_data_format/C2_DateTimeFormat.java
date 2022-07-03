package L1_simple_data_format;

import lombok.extern.log4j.Log4j;
import utils.MyAtomicInteger;

import java.time.format.DateTimeFormatter;

@Log4j
public class C2_DateTimeFormat {

    static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static MyAtomicInteger countErr = new MyAtomicInteger(0);
    static MyAtomicInteger countSuccess = new MyAtomicInteger(0);

    public static void main(String[] args) {


        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                countSuccess.incrementAndGet();
                log.info(dateFormat.parse("1951-04-21"));
            }).start();
        }
    }
}
