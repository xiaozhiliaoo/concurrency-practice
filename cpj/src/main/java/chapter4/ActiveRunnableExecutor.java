package chapter4;

import EDU.oswego.cs.dl.util.concurrent.Channel;

/**
 * @author lili
 * @date 2022/6/19 10:55
 */
public class ActiveRunnableExecutor extends Thread {
    Channel me = null; // ... //  used for all incoming messages

    public void run() {
        try {
            for (; ; ) {
                ((Runnable) (me.take())).run();
            }
        } catch (InterruptedException ie) {
        } // die
    }
}
