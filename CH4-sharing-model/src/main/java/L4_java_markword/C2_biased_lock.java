package L4_java_markword;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/19 0:24
 */
public class C2_biased_lock {
    public static void main(String[] args) {
        System.out.println("[jvm刚启动时, 新建对象x的内存布局: ] ");
        System.out.println(ClassLayout.parseInstance(new Object()).toPrintable());

        C1_markword.sleep(2_000);

        System.out.println("[2秒后, 新建对象y的内存布局: ] ");
        System.out.println(ClassLayout.parseInstance(new Object()).toPrintable());
    }
}
