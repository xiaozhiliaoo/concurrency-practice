package base.queue;

import java.util.concurrent.SynchronousQueue;

/**
 * @author lili
 * @date 2020/5/29 19:33
 * @description
 * @notes
 */
public class SynchronousTest {
    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<String> q = new SynchronousQueue<>();
        System.out.println("0");
        q.put("1");
        System.out.println("1");
        q.put("2");
        System.out.println("2");
        q.put("3");
        System.out.println("3");
        q.put("4");
        System.out.println("4");
    }
}
