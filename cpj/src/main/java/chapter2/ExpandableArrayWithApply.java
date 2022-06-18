package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:43
 */
public class ExpandableArrayWithApply extends ExpandableArray {

    public ExpandableArrayWithApply(int cap) {
        super(cap);
    }

    synchronized void applyToAll(Procedure p) {
        for (int i = 0; i < size; ++i)
            p.apply(data[i]);
    }
}
