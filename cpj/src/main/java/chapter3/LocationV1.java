package chapter3;

/**
 * @author lili
 * @date 2020/5/29 18:33
 * @description
 * @notes
 */
public class LocationV1 {
    private double x_, y_;

    synchronized double x() {
        return x_;
    }

    synchronized double y() {
        return y_;
    }

    synchronized void moveBy(double dx, double dy) {
        x_ += dx;
        y_ += dy;
    }
}
