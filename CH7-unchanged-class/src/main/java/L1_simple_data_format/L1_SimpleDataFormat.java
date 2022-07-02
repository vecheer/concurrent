package L1_simple_data_format;

import lombok.extern.log4j.Log4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author yq
 * @version 1.0
 * @date 2022/7/3 2:31
 */
@Log4j
public class L1_SimpleDataFormat {

    static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            new Thread(()->{
                try {
                    log.info(dateFormat.parse("1951-04-21"));
                } catch (ParseException e) {
                    log.error(e);
                }
            }).start();
        }
    }
}
