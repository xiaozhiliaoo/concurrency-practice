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
//                Thread.interrupted();
                System.out.println("hi!");
//                Thread.currentThread().interrupt();
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(5000);
                    System.out.println("hi!"+i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(Thread.currentThread().getName() + " was interrupted!");
                e.printStackTrace();
            }
        });
        thread.setName("other-thread");
        thread.start();

//        Thread.currentThread().interrupt();
        thread.interrupt();



        for (int i = 0; i < 10; i++) {
            System.out.println(1111);
        }
        try {
            Thread.sleep(5000);
//            Thread.currentThread().interrupt();
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            System.out.println("main thread interupte"+e);
        }
        System.out.println(Thread.currentThread().getName());
    }
}
