package chapter3;

/**
 * @author lili
 * @date 2020/5/29 18:32
 * @description
 * @notes
 */
public class LocationV2 {
    private XY xy_;
    LocationV2(double x, double y) {
        xy_ = new XY(x, y);
    }
    synchronized XY xy() { return xy_; }
    synchronized void moveBy(double dx,double dy) {
        xy_ = new XY(xy_.x() + dx, xy_.y() + dy);
    }
}
