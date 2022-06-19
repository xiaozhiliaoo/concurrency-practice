package chapter4;

import chapter2.Helper;

/**
 * @author lili
 * @date 2022/6/19 10:29
 */
public class HostWithExecutor {                 // Generic code sketch
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
