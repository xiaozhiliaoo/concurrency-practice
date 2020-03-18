package mytest;

/**
 * @packgeName: mytest
 * @ClassName: InterruptedDemo
 * @copyright: CopyLeft
 * @description:<描述>
 * @author: lili
 * @date: 2017/10/6-18:36
 * @version: 1.0
 * @since: JDK 1.8
 */
public class InterruptedDemo implements Runnable {
    @Override
    public void run() {
        try {
            System.out.println("执行任务中.....");
            System.out.println(Thread.currentThread().isInterrupted());
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
//            Thread.interrupted();
//            System.out.println(Thread.interrupted());
            System.out.println(Thread.currentThread().isInterrupted());
        }
    }

    public static void main(String[] args) {
        Thread t = new Thread(new InterruptedDemo());
        t.start();
        t.interrupt();
    }
}
