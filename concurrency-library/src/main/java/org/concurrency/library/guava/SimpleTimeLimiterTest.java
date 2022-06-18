package org.concurrency.library.guava;

import com.google.common.util.concurrent.SimpleTimeLimiter;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lili
 * @date 2020/5/30 17:38
 * @description 指定某个方法或者任务在指定时间内完成。
 * @notes
 */
public class SimpleTimeLimiterTest {
    public static void main(String[] args) {

        ExecutorService service = Executors.newCachedThreadPool();
        SimpleTimeLimiter timeLimiter = SimpleTimeLimiter.create(service);
        TimeLimitImpl timeLimit = new TimeLimitImpl();
        TimeLimit timeLimitProxy = timeLimiter.newProxy(timeLimit, TimeLimit.class, Duration.ofSeconds(3));
        timeLimitProxy.quick();

    /*    try {
            timeLimiter.runWithTimeout(() -> {
                try {
                    TimeUnit.MINUTES.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, Duration.ofMillis(5000));
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


    }
}
