package L3_Unsafe;

import lombok.extern.log4j.Log4j;
import org.springframework.util.StopWatch;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author yq
 * @version 1.0
 * @date 2022/7/2 23:50
 */
@Log4j
public class C2_Unsafe_MyAtomicInteger {
    static MyAtomicInteger count = new MyAtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                log.info(count.getAndIncrement());
            }
        });


        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 49999; i++) {
                log.info(count.getAndDecrement());
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        watch.stop();
        System.out.println("结果: " + count.get() + "  耗时: " + watch.getLastTaskTimeMillis());

    }

    static class MyAtomicInteger{

        private static Unsafe unsafe;
        private static long intValueOffset;


        // reflect to get the Unsafe instance
        static {
            Field field;
            try {
                field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);

                unsafe = (Unsafe) field.get(null);
                intValueOffset = unsafe.objectFieldOffset(MyAtomicInteger.class.getDeclaredField("value"));
            } catch (NoSuchFieldException | IllegalAccessException  ignored) {
            }
        }

        private volatile int value;

        public MyAtomicInteger(int value) {
            this.value = value;
        }

        public int get() {
            return value;
        }

        public int getAndIncrement(){
            int valueCached = value;
            int lastValue;
            while (!unsafe.compareAndSwapInt(this,intValueOffset,(lastValue=value),lastValue+1)){}
            return valueCached;
        }

        public int incrementAndGet (){
            int lastValue;
            while (!unsafe.compareAndSwapInt(this,intValueOffset,(lastValue=value),lastValue+1)){}
            return lastValue+1;
        }

        public int getAndDecrement(){
            int valueCached = value;
            int lastValue;
            while (!unsafe.compareAndSwapInt(this,intValueOffset,(lastValue=value),lastValue-1)){}
            return valueCached;
        }

        public int decrementAndGet(){
            int lastValue;
            while (!unsafe.compareAndSwapInt(this,intValueOffset,(lastValue=value),lastValue-1)){}
            return lastValue-1;
        }

    }

}
