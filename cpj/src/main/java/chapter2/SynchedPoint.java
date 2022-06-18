package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:49
 */
public class SynchedPoint {

    protected final BarePoint delegate = new BarePoint();

    public synchronized double getX() {
        return delegate.x;
    }

    public synchronized double getY() {
        return delegate.y;
    }

    public synchronized void setX(double v) {
        delegate.x = v;
    }

    public synchronized void setY(double v) {
        delegate.y = v;
    }
}