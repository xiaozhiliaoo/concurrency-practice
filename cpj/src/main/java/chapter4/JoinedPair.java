package chapter4;

import java.awt.*;

/**
 * @author lili
 * @date 2022/6/19 10:35
 */
public abstract class JoinedPair extends Box {
    protected Box fst; // one of the boxes
    protected Box snd; // the other one

    protected JoinedPair(Box a, Box b) {
        fst = a;
        snd = b;
    }

    public synchronized void flip() { // swap fst/snd
        Box tmp = fst;
        fst = snd;
        snd = tmp;
    }

    public void show(Graphics g, Point p) {
    }

    public Dimension size() {
        return new Dimension(0, 0);
    }

    public Box duplicate() {
        return null;
    }

    //  other internal helper methods
}
