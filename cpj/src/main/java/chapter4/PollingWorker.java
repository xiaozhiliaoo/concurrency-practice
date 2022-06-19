package chapter4;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author lili
 * @date 2022/6/19 10:34
 */
public class PollingWorker implements Runnable {       // Incomplete
    private java.util.List tasks = new LinkedList(); // ...;
    private long sleepTime = 100; // ...;

    void register(IOEventTask t) {
        tasks.add(t);
    }

    void deregister(IOEventTask t) {
        tasks.remove(t);
    }

    public void run() {
        try {
            for (; ; ) {
                for (Iterator it = tasks.iterator(); it.hasNext(); ) {
                    IOEventTask t = (IOEventTask) (it.next());
                    if (t.done())
                        deregister(t);
                    else {
                        boolean trigger;
                        try {
                            trigger = t.input().available() > 0;
                        } catch (IOException ex) {
                            trigger = true; // trigger if exception on check
                        }
                        if (trigger)
                            t.run();
                    }
                }
                Thread.sleep(sleepTime);
            }
        } catch (InterruptedException ie) {
        }
    }
}