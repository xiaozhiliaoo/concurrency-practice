package chapter4;

import EDU.oswego.cs.dl.util.concurrent.FJTask;

/**
 * @author lili
 * @date 2022/6/19 10:51
 */
public class FibVCB extends FJTask {
    static final int sequentialThreshold = 13; // for tuning

    // ...
    volatile int number = 0;        // as before
    final FibVCB parent;            // Is null for outermost call
    int callbacksExpected = 0;
    volatile int callbacksReceived = 0;

    FibVCB(int n, FibVCB p) {
        number = n;
        parent = p;
    }

    int seqFib(int n) {
        if (n <= 1)
            return n;
        else
            return seqFib(n - 1) + seqFib(n - 2);
    }

    // Callback method invoked by subtasks upon completion
    synchronized void addToResult(int n) {
        number += n;
        ++callbacksReceived;
    }

    public void run() {  // same structure as join-based version
        int n = number;
        if (n <= sequentialThreshold)
            number = seqFib(n);
        else {
            // clear number so subtasks can fill in
            number = 0;
            // establish number of callbacks expected
            callbacksExpected = 2;

            new FibVCB(n - 1, this).fork();
            new FibVCB(n - 2, this).fork();

            // Wait for callbacks from children
            while (callbacksReceived < callbacksExpected) yield();
        }

        // Call back parent
        if (parent != null) parent.addToResult(number);
    }
}
