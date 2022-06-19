package chapter4;

/**
 * @author lili
 * @date 2022/6/19 10:39
 */
public class DualOutputPushStage extends SingleOutputPushStage {
    private PushStage next2 = null;

    protected synchronized PushStage next2() {
        return next2;
    }

    public synchronized void attach2(PushStage s) {
        next2 = s;
    }
}
