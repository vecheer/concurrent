package L2_Atomic;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class C6_AtomicReferenceFieldUpdater {

    public static void main(String[] args) {
        User user = new User(15,"yq");

        AtomicReferenceFieldUpdater updater =AtomicReferenceFieldUpdater.newUpdater(User.class,int.class,"uid");

        updater.compareAndSet(user,null,"张三");
    }

    @Data
    @AllArgsConstructor
    static class User{
        private volatile int uid;
        private volatile String name;
    }

}
