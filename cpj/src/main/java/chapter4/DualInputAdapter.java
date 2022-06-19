package chapter4;

/**
 * @author lili
 * @date 2022/6/19 10:39
 */
public class DualInputAdapter implements PushStage {
    protected final DualInputPushStage stage;

    public DualInputAdapter(DualInputPushStage s) {
        stage = s;
    }

    public void putA(Box p) {
        stage.putB(p);
    }

}
