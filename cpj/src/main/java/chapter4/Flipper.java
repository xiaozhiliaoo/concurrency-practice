package chapter4;

/**
 * @author lili
 * @date 2022/6/19 10:40
 */
public class Flipper extends SingleOutputPushStage
        implements PushStage {
    public void putA(Box p) {
        if (p instanceof JoinedPair)
            ((JoinedPair) p).flip();
        next1().putA(p);
    }
}
