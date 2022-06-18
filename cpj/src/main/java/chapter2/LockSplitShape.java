package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:53
 */
public class LockSplitShape {                     // Incomplete
    protected double x = 0.0;
    protected double y = 0.0;
    protected double width = 0.0;
    protected double height = 0.0;

    protected final Object locationLock = new Object();
    protected final Object dimensionLock = new Object();

    public double x() {
        synchronized (locationLock) {
            return x;
        }
    }

    public double y() {
        synchronized (locationLock) {
            return y;
        }
    }

    public void adjustLocation() {
        synchronized (locationLock) {
            x = 1; // longCalculation1();
            y = 2; // longCalculation2();
        }
    }

    // and so on

}
