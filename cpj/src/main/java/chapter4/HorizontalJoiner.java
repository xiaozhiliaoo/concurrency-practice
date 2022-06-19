package chapter4;

/**
 * @author lili
 * @date 2022/6/19 10:41
 */
public class HorizontalJoiner extends Joiner {
    protected Box join(Box p, Box q) {
        return new HorizontallyJoinedPair(p, q);
    }
}
