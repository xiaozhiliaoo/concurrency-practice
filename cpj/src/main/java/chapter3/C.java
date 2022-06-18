package chapter3;

/**
 * @author lili
 * @date 2022/6/19 2:09
 */
public class C {                                         // Fragments
    private int v;  // invariant: v >= 0

    synchronized void f() {
        v = -1;   // temporarily set to illegal value as flag
        compute();  // possible stop point (*)
        v = 1;      // set to legal value
    }

    synchronized void g() {
        while (v != 0) {
            --v;
            something();
        }
    }

    void compute() {
    }

    void something() {
    }
}
