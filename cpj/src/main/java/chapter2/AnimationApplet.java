package chapter2;

import java.applet.Applet;

/**
 * @author lili
 * @date 2022/6/19 1:50
 */
public class AnimationApplet extends Applet {            // Fragments
    // ...
    int framesPerSecond; // default zero is illegal value

    void animate() {

        try {
            if (framesPerSecond == 0) { // the unsynchronized check
                synchronized (this) {
                    if (framesPerSecond == 0) { // the double-check
                        String param = getParameter("fps");
                        framesPerSecond = Integer.parseInt(param);
                    }
                }
            }
        } catch (Exception e) {
        }

        // ... actions using framesPerSecond ...
    }
}