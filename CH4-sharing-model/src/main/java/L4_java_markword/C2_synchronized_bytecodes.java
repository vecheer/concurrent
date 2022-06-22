package L4_java_markword;

public class C2_synchronized_bytecodes {


    static Object lock = new Object();



    public void func1(){

        synchronized (lock){
            System.out.println("func1 hello");
        }

    }



}
