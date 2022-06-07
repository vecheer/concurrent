package L2_synchronized;

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/7 0:06
 */
public class C3_synchronized_method {

    synchronized void func1(){
        // 方法体
    }

    synchronized void func2(){
        synchronized (this){
            // 方法体
        }
    }

    /*synchronized static void func(){
        // 方法体
    }*/

    static void func(){
        synchronized (C3_synchronized_method.class){

        }
    }
}
