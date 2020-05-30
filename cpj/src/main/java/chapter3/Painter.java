package chapter3;

import java.awt.*;

/**
 * @author lili
 * @date 2020/5/29 19:54
 * @description
 * @notes
 */
public class Painter extends SingleOutputPushStage implements PushStage{
    protected final Color color_;
    public Painter(Color c) {
        super();
        color_ = c;
    }


    public void putA(Box p) {
        p.setColor(color_);
        next1_.putA(p);
    }

}
