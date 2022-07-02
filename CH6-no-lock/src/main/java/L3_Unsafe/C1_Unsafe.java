package L3_Unsafe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author yq
 * @version 1.0
 * @date 2022/7/2 17:04
 */
@Log4j
public class C1_Unsafe {

    static final User user = new User(16666, "yq");
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        // 获取Unsafe对象
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);// 静态字段，可以通过 field.get(null) 获取字段


        // 获取类模型中 字段的地址
        long uidOffset = unsafe.objectFieldOffset(User.class.getDeclaredField("uid"));
        long unameOffset = unsafe.objectFieldOffset(User.class.getDeclaredField("uname"));


        // 开始 cas
        unsafe.compareAndSwapInt(user, uidOffset, /*预期值*/16666, /*替换值*/888);
        unsafe.compareAndSwapObject(user, unameOffset, /*预期值*/"yq", /*替换值*/"F10yd");

        log.info(user);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @sun.misc.Contended
    static class User {
        private int uid;
        private String uname;
    }
}
