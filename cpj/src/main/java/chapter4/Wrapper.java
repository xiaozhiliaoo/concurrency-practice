package chapter4;

import java.awt.*;

/**
 * @author lili
 * @date 2022/6/19 10:40
 */
public class Wrapper extends SingleOutputPushStage
        implements PushStage {
    protected final int thickness;

    public Wrapper(int t) {
        thickness = t;
    }

    public void putA(Box p) {
        Dimension d = new Dimension(thickness, thickness);
        next1().putA(new WrappedBox(p, d));
    }
}
