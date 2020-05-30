package chapter3;

/**
 * @author lili
 * @date 2020/5/29 18:32
 * @description
 * @notes
 */
public class XY { // immutable
    private final double x_, y_;
    XY(double x, double y) { x_ = x; y_ = y; }
    double x() { return x_; }
    double y() { return y_; }
}