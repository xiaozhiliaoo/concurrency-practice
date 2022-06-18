package chapter1;

import java.awt.*;
import java.util.Random;

/**
 * @author lili
 * @date 2022/6/19 1:28
 */
public class Particle {
    protected int x;
    protected int y;
    protected final Random rng = new Random();

    public Particle(int initialX, int initialY) {
        x = initialX;
        y = initialY;
    }

    /**
     * 随机移动
     */
    public synchronized void move() {
        x += rng.nextInt(10) - 5;
        y += rng.nextInt(20) - 10;
    }

    /**
     * 绘制自己
     *
     * @param g
     */
    public void draw(Graphics g) {
        int lx, ly;
        synchronized (this) {
            lx = x;
            ly = y;
        }
        g.drawRect(lx, ly, 10, 10);
    }
}
