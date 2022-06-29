package L1_volatile;

public class C2_singleton_mode_DCL_problem {

    public static void main(String[] args) {

    }


    static class Singleton {

        private static Singleton INSTANCE;

        private Singleton() {
            if (INSTANCE != null) {
                throw new RuntimeException("不允许破坏单例模式!");
            }
        }

        public static Singleton getInstance() {
            if (INSTANCE != null)
                return INSTANCE;

            synchronized (Singleton.class) {
                if (INSTANCE != null)
                    return INSTANCE;

                INSTANCE = new Singleton();
                return INSTANCE;
            }
        }
    }

}
