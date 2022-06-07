package L2_synchronized;

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/7 23:50
 */
public class X_synchronized_question2 {

    synchronized void func1() {
        // 方法体
    }

    synchronized void func2() {
        // 方法体
    }


    synchronized void func3() {
        // 方法体
    }

    static synchronized void func4() {
        // 方法体
    }


    void func11() {
        synchronized (this) {
            // 方法体

        }
    }

    synchronized void func22() {
        synchronized (this) {
            // 方法体
        }
    }


    void func33() {
        synchronized (this) {
            // 方法体
        }
    }

    static void func44() {
        synchronized (X_synchronized_question2.class) {
            // 方法体
        }
    }
}
