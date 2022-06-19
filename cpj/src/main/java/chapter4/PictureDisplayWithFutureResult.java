package chapter4;

import EDU.oswego.cs.dl.util.concurrent.Callable;
import EDU.oswego.cs.dl.util.concurrent.FutureResult;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * @author lili
 * @date 2022/6/19 10:46
 */
public class PictureDisplayWithFutureResult {            // Code sketch

    void displayBorders() {
    }

    void displayCaption() {
    }

    void displayImage(byte[] b) {
    }

    void cleanup() {
    }


    private final Renderer renderer = new StandardRenderer();
    // ...

    public void show(final URL imageSource) {

        try {
            EDU.oswego.cs.dl.util.concurrent.FutureResult futurePic = new FutureResult();
            Runnable command = futurePic.setter(new Callable() {
                public Object call() {
                    return renderer.render(imageSource);
                }
            });
            new Thread(command).start();

            displayBorders();
            displayCaption();

            displayImage(((Pic) (futurePic.get())).getImage());
        } catch (InterruptedException ex) {
            cleanup();
            return;
        } catch (InvocationTargetException ex) {
            cleanup();
            return;
        }
    }
}
