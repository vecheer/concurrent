package L1_question;


class User {
    String name;

    public User(String name) {
        this.name = name;
    }
}

public class C3_volatile_question {
    static User u = null;

    public void question(){

        new Thread(()->{
            u = new User("wyq");
        }).start();

        new Thread(()->{
            String val = u.name;
        }).start();
    }


    public void other(){
        u = new User("wyq ");
    }
}
