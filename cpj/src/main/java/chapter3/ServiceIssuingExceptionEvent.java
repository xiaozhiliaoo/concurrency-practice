package chapter3;

import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList;

import java.util.Iterator;

/**
 * @author lili
 * @date 2022/6/19 2:07
 */
public class ServiceIssuingExceptionEvent {         // Incomplete
    // ...
    private final CopyOnWriteArrayList handlers =
            new CopyOnWriteArrayList();

    public void addHandler(ExceptionEventListener h) {
        handlers.add(h);
    }

    public void service() {
        // ...
        boolean failed = true;
        if (failed) {
            Throwable ex = new ServiceException();
            ExceptionEvent ee = new ExceptionEvent(this, ex);

            for (Iterator it = handlers.iterator(); it.hasNext(); ) {
                ExceptionEventListener l =
                        (ExceptionEventListener) (it.next());
                l.exceptionOccured(ee);
            }
        }
    }

}
