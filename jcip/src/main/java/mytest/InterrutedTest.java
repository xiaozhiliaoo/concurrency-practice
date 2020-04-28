package mytest;

/**
 * @author lili
 * @date 2020/4/28 23:32
 * @description
 * @notes
 */
public class InterrutedTest {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            try {
                System.out.println("hi!");
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " was interrupted!");
                e.printStackTrace();
            }
        });
        thread.setName("other-thread");
        thread.start();
        thread.interrupt();


        for (int i = 0; i < 10; i++) {
            System.out.println(1111);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("main thread interupte"+e);
        }
        System.out.println(Thread.currentThread().getName());
        //System.exit(1);
    }
}
