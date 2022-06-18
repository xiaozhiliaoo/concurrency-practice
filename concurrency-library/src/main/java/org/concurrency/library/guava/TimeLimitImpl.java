package org.concurrency.library.guava;

import java.util.concurrent.TimeUnit;

/**
 * @author lili
 * @date 2020/5/30 17:43
 * @description
 * @notes
 */
public class TimeLimitImpl implements TimeLimit {
    @Override
    public void quick() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            System.out.println(i);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
