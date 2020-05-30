package chapter3;

/**
 * @author lili
 * @date 2020/5/29 18:32
 * @description
 * @notes
 */
public class LocationV3 {
    private XY xy_;

    private synchronized boolean commit(XY oldp,
                                        XY newp) {
        boolean success = (xy_ == oldp);
        if (success) xy_ = newp;
        return success;
    }

    LocationV3(double x, double y) {
        xy_ = new XY(x, y);
    }

    synchronized XY xy() {
        return xy_;
    }

    void moveBy(double dx, double dy) {
        while (!Thread.interrupted()) {
            XY oldp = xy();
            XY newp = new XY(oldp.x() + dx, oldp.y() + dy);
            if (commit(oldp, newp)) break;
            Thread.yield();
        }
    }
}
