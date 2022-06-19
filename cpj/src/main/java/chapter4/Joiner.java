package chapter4;

/**
 * @author lili
 * @date 2022/6/19 10:40
 */
public abstract class Joiner extends SingleOutputPushStage
        implements DualInputPushStage {
    protected Box a = null;  // incoming from putA
    protected Box b = null;  // incoming from putB

    protected abstract Box join(Box p, Box q);

    protected synchronized Box joinFromA(Box p) {
        while (a != null)             // wait until last consumed
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        a = p;
        return tryJoin();
    }

    protected synchronized Box joinFromB(Box p) { // symmetrical
        while (b != null)
            try {
                wait();
            } catch (InterruptedException ie) {
                return null;
            }
        b = p;
        return tryJoin();
    }

    protected synchronized Box tryJoin() {
        if (a == null || b == null) return null; // cannot join
        Box joined = join(a, b);              // make combined box
        a = b = null;                         // forget old boxes
        notifyAll();                          // allow new puts
        return joined;
    }

    public void putA(Box p) {
        Box j = joinFromA(p);
        if (j != null) next1().putA(j);
    }

    public void putB(Box p) {
        Box j = joinFromB(p);
        if (j != null) next1().putA(j);
    }
}
