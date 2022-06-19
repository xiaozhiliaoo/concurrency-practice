package chapter4;

import java.net.URL;

/**
 * @author lili
 * @date 2022/6/19 10:46
 */
public class PicturAppWithFuture {                   // Code sketch
    private final Renderer renderer = new AsynchRenderer();

    void displayBorders() {
    }

    void displayCaption() {
    }

    void displayImage(byte[] b) {
    }

    void cleanup() {
    }

    public void show(final URL imageSource) {
        Pic pic = renderer.render(imageSource);

        displayBorders();  // do other things ...
        displayCaption();

        byte[] im = pic.getImage();
        if (im != null)
            displayImage(im);
        else {
        } // deal with assumed rendering failure
    }
}
