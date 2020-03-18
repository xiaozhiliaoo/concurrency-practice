package chapter3;

import java.util.concurrent.*;

/**
 * Created by lili on 2017/4/25.
 */

public class RejectThreadPoolDemo {
    public static class MyTask implements Runnable{

        @Override
        public void run() {
            System.out.println(System.currentTimeMillis()+":Thread Id:" + Thread.currentThread().getId());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyTask myTask = new MyTask();
        ExecutorService es = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(),  //永远放不进去，只有去拿的时候才可以有数据
                Executors.defaultThreadFactory(),
                //拒绝策略  负载太多了怎么办？？？
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println(r.toString()+"is discard");
                    }
        });

        for (int i = 0; i < Integer.MAX_VALUE; i++) {

            es.submit(myTask);
            //10s提交一个任务，
            Thread.sleep(10);
        }
    }

}
