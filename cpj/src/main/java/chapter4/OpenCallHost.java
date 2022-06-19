package chapter4;

import chapter2.Helper;

/**
 * @author lili
 * @date 2022/6/19 10:27
 */
public class OpenCallHost {                     // Generic code sketch
    protected long localState;
    protected final Helper helper = new Helper();

    protected synchronized void updateState() {
        localState = 2; // ...;
    }

    public void req() {
        updateState();
        helper.handle();
    }
}