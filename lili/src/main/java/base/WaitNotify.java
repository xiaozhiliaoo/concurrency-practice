package base;

/**
 * @author lili
 * @date 2020/4/14 11:30
 * @description
 * @notes
 */
public class WaitNotify {
    public static void main(String[] args) throws InterruptedException {
        String aaa = "lili";


        Runnable task1 = () -> {
            synchronized (aaa) {
                try {
                    aaa.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("i am start ...");
            }
        };
        new Thread(task1).start();

        Runnable task2 = () -> {
            synchronized (aaa) {
                aaa.notify();
            }
        };
        new Thread(task2).start();
    }
}
