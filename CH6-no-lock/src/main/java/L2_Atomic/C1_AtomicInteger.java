package L2_Atomic;

import lombok.extern.log4j.Log4j;

import java.util.concurrent.atomic.AtomicInteger;

@Log4j
public class C1_AtomicInteger {
    public static void main(String[] args) {
        AtomicInteger num = new AtomicInteger(100);

        num.incrementAndGet();
        num.getAndIncrement();
        num.decrementAndGet();
        num.getAndDecrement();


        num.getAndAdd(10);
        num.addAndGet(10);


        num.getAndUpdate(x->{
            if (x>5) {
                log.info("x>5");
                return x;
            } else
                return 0;
        });

        System.out.println(num);


    }
}
