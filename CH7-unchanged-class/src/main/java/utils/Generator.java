package utils;

import java.util.UUID;

public class Generator {
    public static String getUUID(){
        return UUID.randomUUID().toString();
    }
}
