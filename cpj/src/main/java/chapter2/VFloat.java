package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:46
 */
public final class VFloat {
    private float value;

    final synchronized void set(float f) {
        value = f;
    }

    final synchronized float get() {
        return value;
    }
}
