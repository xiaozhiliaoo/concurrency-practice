package chapter3;

import java.util.Iterator;

/**
 * @author lili
 * @date 2022/6/19 10:17
 */
public class Subject {

    protected double val = 0.0;       // modeled state
    protected final EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList observers =
            new EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList();

    public synchronized double getValue() {
        return val;
    }

    protected synchronized void setValue(double d) {
        val = d;
    }

    public void attach(Observer o) {
        observers.add(o);
    }

    public void detach(Observer o) {
        observers.remove(o);
    }

    public void changeValue(double newstate) {
        setValue(newstate);
        for (Iterator it = observers.iterator(); it.hasNext(); )
            ((Observer) (it.next())).changed(this);
    }

}
