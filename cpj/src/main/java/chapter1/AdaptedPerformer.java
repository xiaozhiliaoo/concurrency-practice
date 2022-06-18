package chapter1;

/**
 * @author lili
 * @date 2022/6/19 1:34
 */
public class AdaptedPerformer implements Runnable {
    private final Performer adaptee;

    public AdaptedPerformer(Performer p) {
        adaptee = p;
    }

    public void run() {
        adaptee.perform();
    }
}
