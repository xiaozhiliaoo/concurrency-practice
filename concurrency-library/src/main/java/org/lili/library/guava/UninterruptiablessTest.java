package org.lili.library.guava;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.Uninterruptibles;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;

/**
 * @author lili
 * @date 2020/5/30 19:17
 * @description
 * @notes
 */
public class UninterruptiablessTest {
    public static void main(String[] args) throws TimeoutException, InterruptedException {

        ExecutorService service = Executors.newCachedThreadPool();
        SimpleTimeLimiter timeLimiter = SimpleTimeLimiter.create(service);
        timeLimiter.runWithTimeout(()->{
            Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
        }, Duration.ofSeconds(1));
    }
}
