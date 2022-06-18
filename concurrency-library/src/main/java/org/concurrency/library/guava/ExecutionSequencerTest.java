package org.concurrency.library.guava;

import com.google.common.util.concurrent.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lili
 * @date 2020/5/30 17:57
 * @description https://github.com/google/guava/wiki/ListenableFutureExplained
 * https://wiki.jikexueyuan.com/project/google-guava-official-tutorial/service-framework.html
 * @notes
 */
public class ExecutionSequencerTest {

    public static void main(String[] args) {

        ExecutorService service = Executors.newCachedThreadPool();

        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(service);

        ListenableFuture<String> listenableFuture = listeningExecutorService.submit(() -> "return my result");

        listenableFuture.addListener(() -> System.out.println("before done."), service);

        Futures.addCallback(listenableFuture, new FutureCallback<String>() {

            @Override
            public void onSuccess(@Nullable String result) {
                //成功之后结果
                System.out.println("success:" + result);
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("failure:" + t.toString());
            }
        }, listeningExecutorService);

        try {
            String s = listenableFuture.get();
            System.out.println(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        List<ListenableFuture<String>> queries = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            listenableFuture = listeningExecutorService.submit(() -> "return my result"+ finalI);
            queries.add(listenableFuture);
        }
        //全部成功才可以返回
        ListenableFuture<List<String>> listListenableFuture = Futures.successfulAsList(queries);

    }
}
