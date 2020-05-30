package chapter3;

import java.awt.*;

/**
 * @author lili
 * @date 2020/5/29 19:50
 * @description
 * @notes
 */
public abstract class Box {
    protected Color color = Color.white;

    public synchronized Color getColor() {
        return color;
    }

    public synchronized void setColor(Color c) {
        color = c;
    }

    public abstract java.awt.Dimension size();

    public abstract Box duplicate();                 // clone

    public abstract void show(Graphics g, Point origin);// display
}
