package chapter3;

import EDU.oswego.cs.dl.util.concurrent.Channel;

/**
 * @author lili
 * @date 2020/5/29 19:04
 * @description
 * @notes
 */
public interface Executor {
    void execute(Runnable r);
}

class HostWithExecutor {                 // Generic code sketch
    protected long localState;
    protected final Helper helper = new Helper();
    protected final Executor executor;

    public HostWithExecutor(Executor e) {
        executor = e;
    }

    protected synchronized void updateState() {
        localState = 2; // ...;
    }

    public void req() {
        updateState();
        executor.execute(new Runnable() {
            public void run() {
                helper.handle();
            }
        });
    }
}

class PlainWorkerPool implements Executor {

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
        for (int i = 0; i < nworkers; ++i) {
            activate();
        }
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
