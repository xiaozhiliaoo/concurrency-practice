package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:52
 */
public class Shape {                                   // Incomplete
    protected double x = 0.0;
    protected double y = 0.0;
    protected double width = 0.0;
    protected double height = 0.0;

    public synchronized double x() {
        return x;
    }

    public synchronized double y() {
        return y;
    }

    public synchronized double width() {
        return width;
    }

    public synchronized double height() {
        return height;
    }

    public synchronized void adjustLocation() {
        x = 1; // longCalculation1();
        y = 2; //longCalculation2();
    }

    public synchronized void adjustDimensions() {
        width = 3; // longCalculation3();
        height = 4; // longCalculation4();
    }

    // ...
}
