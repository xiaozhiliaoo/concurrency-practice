package chapter3;

/**
 * @author lili
 * @date 2020/5/29 19:54
 * @description
 * @notes
 */
public class DualOutputPushStage extends SingleOutputPushStage{
    protected PushStage next2_ = null;
    void attach2(PushStage s) { next2_ = s; }
}
