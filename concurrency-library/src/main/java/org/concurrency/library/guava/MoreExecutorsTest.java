package org.concurrency.library.guava;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.ExecutionException;

/**
 * @author lili
 * @date 2020/4/27 1:58
 * @description
 * @notes
 */
public class MoreExecutorsTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ListeningExecutorService listeningExecutorService = MoreExecutors.newDirectExecutorService();
        ListenableFuture<String> lili = (ListenableFuture<String>) listeningExecutorService.submit(() -> System.out.println("lili"));
        System.out.println(lili.get());

    }
}
