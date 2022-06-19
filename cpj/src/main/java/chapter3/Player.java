package chapter3;

import EDU.oswego.cs.dl.util.concurrent.Latch;

/**
 * @author lili
 * @date 2022/6/19 10:13
 */
public class Player implements Runnable {              // Code sketch
    // ...
    protected final Latch startSignal;

    Player(Latch l) {
        startSignal = l;
    }

    public void run() {
        try {
            startSignal.acquire();
            play();
        } catch (InterruptedException ie) {
            return;
        }
    }

    void play() {
    }
    // ...
}
