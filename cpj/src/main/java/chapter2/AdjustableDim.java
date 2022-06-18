package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:53
 */
public class AdjustableDim {
    protected double width;
    protected double height;

    public AdjustableDim(double initW, double initH) {
        width = initW;
        height = initH;
    }

    public synchronized double width() {
        return width;
    }

    public synchronized double height() {
        return height;
    }

    public synchronized void adjust() {
        width = longCalculation3();
        height = longCalculation4();
    }

    protected double longCalculation3() {
        return 3; /* ... */
    }

    protected double longCalculation4() {
        return 4; /* ... */
    }

}
