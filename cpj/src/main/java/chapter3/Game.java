package chapter3;

import EDU.oswego.cs.dl.util.concurrent.Latch;

/**
 * @author lili
 * @date 2022/6/19 10:13
 */
public class Game {
    // ...
    void begin(int nplayers) {
        Latch startSignal = new Latch();

        for (int i = 0; i < nplayers; ++i)
            new Thread(new Player(startSignal)).start();

        startSignal.release();
    }
}
