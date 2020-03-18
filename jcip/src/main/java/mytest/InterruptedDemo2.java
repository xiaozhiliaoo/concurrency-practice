package mytest;

/**
 * @packgeName: mytest
 * @ClassName: InterruptedDemo2
 * @copyright: CopyLeft
 * @description:<描述>
 * @author: lili
 * @date: 2017/10/6-18:42
 * @version: 1.0
 * @since: JDK 1.8
 */
public class InterruptedDemo2 implements Runnable{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().isInterrupted());
        System.out.println("执行任务中.....");
//        System.out.println(Thread.interrupted());//尝试注释掉这里看结果  清除中断状态
        System.out.println(Thread.currentThread().isInterrupted());
        System.out.println(Thread.currentThread().isInterrupted());

        for (int i = 0; i < 1000000000; i++) {
            System.out.println(1);
        }
        System.out.println(Thread.currentThread().isInterrupted());
        System.out.println(Thread.currentThread().isInterrupted());



    }

    public static void main(String[] args) {
        Thread t = new Thread(new InterruptedDemo2());
        t.start();
        t.interrupt();
    }
}


