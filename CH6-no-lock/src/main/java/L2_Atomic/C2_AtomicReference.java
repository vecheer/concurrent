package L2_Atomic;

import java.util.concurrent.atomic.AtomicReference;

public class C2_AtomicReference {

    public static void main(String[] args) {
        AtomicReference<MyNum> myNum = new AtomicReference<>();


    }


    static class MyNum{
        private int value;

        public void add(int d){
            value += d;
        }
    }
}
