package chapter3;

/**
 * Created by lili on 2017/4/24.
 */
public class ThreadPoolTest {


    public static class MyTask implements Runnable {

        @Override
        public void run() {
            //线程休眠10s后在执行任务，而不是每次执行前休息10s，加了同步后，每次只有一个线程进入临界区
            // 相当于串行，每次都要等十秒
            long start = System.currentTimeMillis();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(System.currentTimeMillis() + "Thread Id" + Thread.currentThread().getName());
            System.out.println(System.currentTimeMillis() - start);

        }
    }


    public static void main(String[] args) throws InterruptedException {

        MyTask task = new MyTask();

        //线程复用的.........
        /*ExecutorService es = Executors.newFixedThreadPool(5);
        for(int i=0;i<50;i++){
            //es.submit(task);
            es.execute(task);
        }*/

        /*Thread[] t = new Thread[50];
        for (int i = 0; i < 50; i++) {
            t[i] = new Thread(task);
        }
        for (int i = 0; i < 50; i++) {
            t[i].start();
        }
        for (int i = 0; i < 50; i++) {
            t[i].join();
        }*/


        /*MyTask t1 = new MyTask();
        MyTask t2 = new MyTask();
        MyTask t3 = new MyTask();
        MyTask t4 = new MyTask();
        MyTask t5 = new MyTask();
        for(int i=0;i<10;i++){
            //Thread.sleep(1000);
            new Thread(t1).start();
            new Thread(t2).start();
            new Thread(t3).start();
            new Thread(t4).start();
            new Thread(t5).start();
            //System.out.print(i);
        }*/

       /* MyTask t1 = new MyTask();
        for(int i=0;i<50;i++){
            //Thread.sleep(1000);
            new Thread(t1).start();
        }*/



    }
}

