package chapter4;

import java.awt.*;

/**
 * @author lili
 * @date 2022/6/19 10:40
 */
public class Painter extends SingleOutputPushStage
        implements PushStage {
    protected final Color color; // the color to paint things

    public Painter(Color c) {
        color = c;
    }

    public void putA(Box p) {
        p.setColor(color);
        next1().putA(p);
    }
}
