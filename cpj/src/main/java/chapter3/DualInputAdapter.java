package chapter3;

/**
 * @author lili
 * @date 2020/5/29 19:52
 * @description
 * @notes
 */
public class DualInputAdapter implements PushStage {
    protected final DualInputPushStage stage_;

    DualInputAdapter(DualInputPushStage stage) {
        stage_ = stage;
    }

    @Override
    public void putA(Box p) {
        stage_.putB(p);
    }
}
