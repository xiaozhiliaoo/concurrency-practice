package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:59
 */
public class Dot {
    protected ImmutablePoint loc;

    public Dot(int x, int y) {
        loc = new ImmutablePoint(x, y);
    }

    public synchronized ImmutablePoint location() {
        return loc;
    }

    protected synchronized void updateLoc(ImmutablePoint newLoc) {
        loc = newLoc;
    }

    public void moveTo(int x, int y) {
        updateLoc(new ImmutablePoint(x, y));
    }

    public synchronized void shiftX(int delta) {
        updateLoc(new ImmutablePoint(loc.x() + delta,
                loc.y()));
    }
}