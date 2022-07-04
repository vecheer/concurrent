package test;

/**
 * @author yq
 * @version 1.0
 * @date 2022/7/4 20:47
 */
// 非静态内部类应该如何新建？
public class Test2 {

    public static void main(String[] args) {
        // [wrong]
        // A.SubA() sub = new A.SubA();

        // [right]
        A a = new A();
        A.SubA sub = a.new SubA();

        a = null;
        System.out.println(sub);
    }
}


class A{

    int sum;

    class SubA{
        public void print(){
            System.out.println(sum);
        }
    }
}