package chapter3;

import java.util.concurrent.*;

/**
 * Created by lili on 2017/4/24.
 */
public class ExtThreadPool {
    public static class MyTask implements Runnable{

        public String name;

        public MyTask(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println("正在执行"+"Thread Id:" + Thread.currentThread().getId()+", Task Name = " + name);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es;
        es = new ThreadPoolExecutor(5,
                5,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>()){
            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                System.out.println("准备执行：" + ((MyTask)r).name);
            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                System.out.println("执行完成：" + ((MyTask)r).name);
            }

            @Override
            protected void terminated() {
                System.out.println("线程退出");
            }
        };

        for (int i = 0; i < 5; i++) {
            MyTask myTask = new MyTask("Task-GEYM-" + i);
            es.execute(myTask);
            Thread.sleep(1000);
        }

        es.shutdown();



    }
}
