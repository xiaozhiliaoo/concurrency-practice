package chapter3;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by lili on 2017/4/24.
 */
public class ScheduledExecutorServiceDemo {

    public static void main(String[] args) {

        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

        service.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    System.out.println(System.currentTimeMillis()/1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0,2, TimeUnit.SECONDS);
    }
}
