package utils.data;

import utils.atomic.MyAtomicInteger;

import java.util.UUID;

public class Generator {



    public static String getUUID(){
        return UUID.randomUUID().toString();
    }

    private static MyAtomicInteger count = new MyAtomicInteger(0);
    public static String getRandomName(String baseName){
        return baseName + "-" + count.getAndDecrement();
    }
}
