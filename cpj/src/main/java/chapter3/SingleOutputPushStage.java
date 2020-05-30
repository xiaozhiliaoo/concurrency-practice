package chapter3;

/**
 * @author lili
 * @date 2020/5/29 19:53
 * @description
 * @notes
 */
public class SingleOutputPushStage {
    protected PushStage next1_= null;
    void attach1(PushStage s) { next1_ = s; }
}
