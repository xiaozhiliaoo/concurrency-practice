package chapter4;

/**
 * @author lili
 * @date 2022/6/19 10:41
 */
public class VerticalJoiner extends Joiner {
    protected Box join(Box p, Box q) {
        return new VerticallyJoinedPair(p, q);
    }
}
