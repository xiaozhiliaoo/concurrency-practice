package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:59
 */
public class ImmutablePoint {
    private final int x;
    private final int y;

    public ImmutablePoint(int initX, int initY) {
        x = initX;
        y = initY;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }
}
