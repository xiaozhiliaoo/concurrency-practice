package chapter4;

/**
 * @author lili
 * @date 2022/6/19 10:42
 */
public class Cloner extends DualOutputPushStage
        implements PushStage {

    public void putA(Box p) {
        final Box p2 = p.duplicate();
        next1().putA(p);
        new Thread(new Runnable() {
            public void run() {
                next2().putA(p2);
            }
        }).start();
    }

}
