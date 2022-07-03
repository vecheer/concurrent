package M2_async_mode;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j;
import utils.data.Generator;

import java.util.LinkedList;

/**
 * 生产者消费者模式
 *
 * 生产者 发送消息
 * 消费者 消费消息
 *
 * 从业务的角度出发，生产和消费应该在不同端，防止消息被堆积
 */
@Log4j
public class M1_Producer_Consumer_Mode {


    // 消息
    // 只允许获取，除非创建的时候给参数赋值
    @Getter
    @AllArgsConstructor
    @ToString
    static final class Message{
        private String id;
        private Object content;
    }


    // 消息队列
    // 实现方式：双向队列
    static class MessageQueue{

        private final LinkedList<Message> queue;
        private int capacity;  // 队列长

        public MessageQueue(int capacity) {
            this.queue = new LinkedList<>();
            this.capacity = capacity;
        }

        // 判空
        public boolean isEmpty(){
            return queue.isEmpty();
        }
        // 判满
        public boolean isFull(){
            return queue.size()==capacity;
        }



        // 消费 消息
        // 实现方式：没消息时，等待
        public Message take() throws InterruptedException {
            synchronized (this){
                while (this.isEmpty()){
                    // while 防止被同为消费者的线程唤醒
                    log.info("队列为空，消费者等待");
                    this.wait();
                }
                this.notifyAll(); // 消费后，去唤醒一下生产者
                return queue.removeLast();
            }
        }

        // 生产 消息
        // 实现方式：消息满时，等待
        public void put(Message msg) throws InterruptedException {
            synchronized (this){
                while (this.isFull()) {
                    // while 防止被同为生产者的线程唤醒
                    log.info("队列满了，生产者等待");
                    this.wait();
                }
                queue.addFirst(msg);
                this.notifyAll();
            }
        }

    }


    public static void main(String[] args) {
        MessageQueue mq = new MessageQueue(3);


        for (int i = 0; i < 7; i++) {
            int index = i;
            new Thread(()->{
                try {
                    mq.put(new Message(Generator.getUUID(),"hello "+ index));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"provider-"+i).start();
        }

        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                try {
                    Message m = mq.take();
                    log.info("消费者来消费了!消费的结果是: " + m.id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"consumer-"+i).start();
        }
    }

}
