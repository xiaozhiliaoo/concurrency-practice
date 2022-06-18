package chapter2;

import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
import EDU.oswego.cs.dl.util.concurrent.SynchronizedDouble;

/**
 * @author lili
 * @date 2022/6/19 1:54
 */
public class Person {                             // Fragments
    // ...
    protected final SynchronizedInt age = new SynchronizedInt(0);

    protected final SynchronizedBoolean isMarried =
            new SynchronizedBoolean(false);

    protected final SynchronizedDouble income =
            new SynchronizedDouble(0.0);

    public int getAge() {
        return age.get();
    }

    public void birthday() {
        age.increment();
    }
    // ...
}