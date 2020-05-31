package chapter3;


import java.applet.Applet;

/**
 * @author lili
 * @date 2020/5/30 21:15
 * @description
 * @notes
 */
public class ParticleApplet extends Applet {

    protected Thread[] threads; // null when not running

    protected final ParticleCanvas canvas = new ParticleCanvas(100);

    public void init() {
        add(canvas);
    }

    protected Thread makeThread(final Particle p) { // utility
        Runnable runloop = new Runnable() {
            public void run() {
                try {
                    for (; ; ) {
                        p.move();
                        canvas.repaint();
                        Thread.sleep(100); // 100msec is arbitrary
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        };
        return new Thread(runloop);
    }

    public synchronized void start() {
        int n = 10; // just for demo

        if (threads == null) { // bypass if already started
            Particle[] particles = new Particle[n];
            for (int i = 0; i < n; ++i) {
                particles[i] = new Particle(50, 50);
            }
            canvas.setParticles(particles);

            threads = new Thread[n];
            for (int i = 0; i < n; ++i) {
                threads[i] = makeThread(particles[i]);
                threads[i].start();
            }
        }
    }

    public synchronized void stop() {
        if (threads != null) { // bypass if already stopped
            for (int i = 0; i < threads.length; ++i)
                threads[i].interrupt();
            threads = null;
        }
    }

    public static void main(String[] args) {
        ParticleApplet applet = new ParticleApplet();
        applet.start();
    }
}
