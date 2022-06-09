package L2_synchronized;

import org.junit.Test;

import java.util.Hashtable;

/**
 * 对于并发安全的类，用户在使用时，未必用的比较安全
 * @author yq
 * @version 1.0
 * @date 2022/6/10 0:07
 */
public class C4_bad_use_of_concurrent_safe_class {

    static Hashtable<String,Integer> hashtable;
    static {
        hashtable = new Hashtable<>();
        hashtable.put("num",1);
    }

    @Test
    public void badUse() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            if (hashtable.get("num") == 1) {
                hashtable.put("num", hashtable.get("num") - 1);
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            if (hashtable.get("num") == 1) {
                hashtable.put("num", hashtable.get("num") - 1);
            }
        }, "t2");


        System.out.println(hashtable.get("num"));
    }
}
