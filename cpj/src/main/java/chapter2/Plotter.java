package chapter2;

import java.awt.*;

/**
 * @author lili
 * @date 2022/6/19 1:46
 */
public class Plotter {                                  // fragments
    // ...

    public void showNextPoint() {
        Point p = new Point();
        p.x = computeX();
        p.y = computeY();
        display(p);
    }

    int computeX() {
        return 1;
    }

    int computeY() {
        return 1;
    }

    protected void display(Point p) {
        // somehow arrange to show p.
    }
}
