package chapter4;

import java.net.URL;

/**
 * @author lili
 * @date 2022/6/19 10:45
 */
public class PictureApp {                       // Code sketch
    // ...
    private final Renderer renderer = new StandardRenderer();

    void displayBorders() {
    }

    void displayCaption() {
    }

    void displayImage(byte[] b) {
    }

    void cleanup() {
    }

    public void show(final URL imageSource) {

        class Waiter implements Runnable {
            private Pic result = null;

            Pic getResult() {
                return result;
            }

            public void run() {
                result = renderer.render(imageSource);
            }
        }
        ;

        Waiter waiter = new Waiter();
        Thread t = new Thread(waiter);
        t.start();

        displayBorders();  // do other things
        displayCaption();  //  while rendering

        try {
            t.join();
        } catch (InterruptedException e) {
            cleanup();
            return;
        }

        Pic pic = waiter.getResult();
        if (pic != null)
            displayImage(pic.getImage());
        else {
        }
        // ... deal with assumed rendering failure
    }
}
