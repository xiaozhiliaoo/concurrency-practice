package chapter4;

import java.awt.*;

/**
 * @author lili
 * @date 2022/6/19 10:43
 */
public class BasicBoxSource extends SingleOutputPushStage
        implements PushSource, Runnable {

    protected final Dimension size;     // maximum sizes
    protected final int productionTime; // simulated delay

    public BasicBoxSource(Dimension s, int delay) {
        size = s;
        productionTime = delay;
    }

    protected Box produce() {
        return new BasicBox((int) (Math.random() * size.width) + 1,
                (int) (Math.random() * size.height) + 1);
    }

    public void start() {
        next1().putA(produce());
    }

    public void run() {
        try {
            for (; ; ) {
                start();
                Thread.sleep((int) (Math.random() * 2 * productionTime));
            }
        } catch (InterruptedException ie) {
        } // die
    }

}
