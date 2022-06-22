package L5_synchronized_optimize;

public class C1_light_lock {
    Object lock = new Object();

    public void func1(){
        synchronized (lock){
            func2();
        }
    }


    public void func2(){
        synchronized (lock){
            // do sth
            int a = 3;
        }
    }


    public void func3(){
        synchronized (lock){
            // do work1
            int b =5;
            synchronized (lock){
                // do work2
                int a = 3;
            }
        }
    }
}
