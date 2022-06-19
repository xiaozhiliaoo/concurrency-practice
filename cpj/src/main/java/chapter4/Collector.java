package chapter4;

/**
 * @author lili
 * @date 2022/6/19 10:41
 */
public class Collector extends SingleOutputPushStage
        implements DualInputPushStage {
    public void putA(Box p) {
        next1().putA(p);
    }

    public void putB(Box p) {
        next1().putA(p);
    }
}
