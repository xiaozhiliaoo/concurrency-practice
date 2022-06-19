package chapter4;

/**
 * @author lili
 * @date 2022/6/19 10:43
 */
public class MaxSizePredicate implements BoxPredicate {

    protected final int max; // max size to let through

    public MaxSizePredicate(int maximum) {
        max = maximum;
    }

    public boolean test(Box p) {
        return p.size().height <= max && p.size().width <= max;
    }
}
