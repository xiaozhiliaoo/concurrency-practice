package chapter2;

import EDU.oswego.cs.dl.util.concurrent.Mutex;

import java.util.Random;

/**
 * @author lili
 * @date 2022/6/19 2:01
 */
public class ParticleUsingWrapper {                     // Incomplete

    protected int x;
    protected int y;
    protected final Random rng = new Random();

    protected final WithMutex withMutex =
            new WithMutex(new Mutex());

    protected void doMove() {
        x += rng.nextInt(10) - 5;
        y += rng.nextInt(20) - 10;
    }

    public void move() {
        try {
            withMutex.perform(new Runnable() {
                public void run() {
                    doMove();
                }
            });
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
    // ...
}
