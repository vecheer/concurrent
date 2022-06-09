package L1_question;

/**
 * @author yq
 * @version 1.0
 * @date 2022/6/9 12:01
 */


class Parent{

    void priFunc(){
        func(5);
    }

    void func(int p){
        System.out.println("parent here");
    }
}

class Sub extends Parent{
    void func(int p){
        System.out.println("Sub here");
    }
}

public class C2_parent_sub_method_invoke {
    public static void main(String[] args) {

        Parent parent = new Sub();
        parent.priFunc();

    }
}
