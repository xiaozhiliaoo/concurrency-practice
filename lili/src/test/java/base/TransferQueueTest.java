package base;

import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

/**
 * @author lili
 * @date 2020/5/28 10:11
 * @description https://puredanger.github.io/tech.puredanger.com/2009/02/28/java-7-transferqueue/
 * @notes
 */
public class TransferQueueTest {
    @Test
    public void test() throws InterruptedException {
        TransferQueue transferQueue = new LinkedTransferQueue();

        for (int i = 0; i < 10000; i++) {

            transferQueue.transfer(i);
        }

        new Thread(()->{
            try {
                System.out.println(transferQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

}
