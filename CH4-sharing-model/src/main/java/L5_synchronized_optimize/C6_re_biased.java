package L5_synchronized_optimize;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.Vector;

@Slf4j
public class C6_re_biased {
    public static void main(String[] args) {
        Vector<Object> list = new Vector<>();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                Object d = new Object();
                list.add(d);
                synchronized (d) {
                    log.info(ClassLayout.parseInstance(d).toPrintable());
                }
            }
            synchronized (list) {
                list.notify();
            }
        }, "t1");
        t1.start();

        Thread t2 = new Thread(() -> {
            synchronized (list) {
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("===============> ");
            for (int i = 0; i < 30; i++) {
                Object d = list.get(i);
                log.info(ClassLayout.parseInstance(d).toPrintable());
                synchronized (d) {
                    log.info( ClassLayout.parseInstance(d).toPrintable());
                }
                log.info(ClassLayout.parseInstance(d).toPrintable());
            }
        }, "t2");
        t2.start();
    }
}
