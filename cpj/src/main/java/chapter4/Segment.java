package chapter4;


/**
 * @author lili
 * @date 2022/6/19 10:53
 */
public class Segment implements Runnable {            // Code sketch
    final CyclicBarrier bar; // shared by all segments

    Segment(CyclicBarrier b) {
        bar = b;
    }

    void update() {
    }

    public void run() {
        // ...
        try {
            for (int i = 0; i < 10 /* iterations */; ++i) {
                update();
                bar.barrier();
            }
        } catch (InterruptedException ie) {
        }
        // ...
    }
}
