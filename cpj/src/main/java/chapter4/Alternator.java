package chapter4;

/**
 * @author lili
 * @date 2022/6/19 10:42
 */
public class Alternator extends DualOutputPushStage
        implements PushStage {
    protected boolean outTo2 = false; // control alternation

    protected synchronized boolean testAndInvert() {
        boolean b = outTo2;
        outTo2 = !outTo2;
        return b;
    }

    public void putA(final Box p) {
        if (testAndInvert())
            next1().putA(p);
        else {
            new Thread(new Runnable() {
                public void run() {
                    next2().putA(p);
                }
            }).start();
        }
    }
}
