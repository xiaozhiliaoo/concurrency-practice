package chapter4.callable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by lili on 2017/4/28.
 */

public class FutureMain {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // new RealData()  模拟业务逻辑  把业务逻辑同步调用改成异步调用
        FutureTask<String> future = new FutureTask<String>(new RealData("a"));
        ExecutorService es = Executors.newFixedThreadPool(1);
        es.submit(future);
        System.out.println("请求完毕");
        System.out.println("请求Controller");
        System.out.println("请求Service");
        System.out.println("请求Dao");
        Thread.sleep(5000);
        System.out.println("数据："+ future.get());
    }
}
