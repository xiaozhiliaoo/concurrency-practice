package chapter2;

/**
 * @author lili
 * @date 2022/6/19 2:00
 */
public class OptimisticDot {
    protected ImmutablePoint loc;

    public OptimisticDot(int x, int y) {
        loc = new ImmutablePoint(x, y);
    }

    public synchronized ImmutablePoint location() {
        return loc;
    }

    protected synchronized boolean commit(ImmutablePoint assumed,
                                          ImmutablePoint next) {
        if (loc == assumed) {
            loc = next;
            return true;
        } else
            return false;
    }

    public synchronized void moveTo(int x, int y) {
        // bypass commit since unconditional
        loc = new ImmutablePoint(x, y);
    }

    public void shiftX(int delta) {
        boolean success = false;
        do {
            ImmutablePoint old = location();
            ImmutablePoint next = new ImmutablePoint(old.x() + delta,
                    old.y());
            success = commit(old, next);
        } while (!success);
    }

}