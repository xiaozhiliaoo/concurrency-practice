package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:46
 */
public final class SetCheck {
    private int a = 0;
    private long b = 0;

    void set() {
        a = 1;
        b = -1;
    }

    boolean check() {
        return ((b == 0) ||
                (b == -1 && a == 1));
    }
}
