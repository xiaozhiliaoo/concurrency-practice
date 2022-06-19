package chapter4;

import EDU.oswego.cs.dl.util.concurrent.Channel;

/**
 * @author lili
 * @date 2022/6/19 10:32
 */
public class PlainWorkerPool implements Executor {
    protected final Channel workQueue;

    public void execute(Runnable r) {
        try {
            workQueue.put(r);
        } catch (InterruptedException ie) { // postpone response
            Thread.currentThread().interrupt();
        }
    }

    public PlainWorkerPool(Channel ch, int nworkers) {
        workQueue = ch;
        for (int i = 0; i < nworkers; ++i) activate();
    }

    protected void activate() {
        Runnable runLoop = new Runnable() {
            public void run() {
                try {
                    for (; ; ) {
                        Runnable r = (Runnable) (workQueue.take());
                        r.run();
                    }
                } catch (InterruptedException ie) {
                } // die
            }
        };
        new Thread(runLoop).start();
    }
}
