package L2_synchronized;

public class X3_volatile_question {

    //int num = 10;

    boolean flag = true;


    public void test(Object o){
        new Thread(()->{
            //num++;
            flag = false;
        }).start();

        if(o == null){
            int a =1;
        }
        if(null == o){
            int a =1;
        }
    }


}
