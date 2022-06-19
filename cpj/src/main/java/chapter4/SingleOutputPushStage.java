package chapter4;

/**
 * @author lili
 * @date 2022/6/19 10:39
 */
public class SingleOutputPushStage {
    private PushStage next1 = null;

    protected synchronized PushStage next1() {
        return next1;
    }

    public synchronized void attach1(PushStage s) {
        next1 = s;
    }
}
