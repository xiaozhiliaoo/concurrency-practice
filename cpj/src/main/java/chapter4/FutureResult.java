package chapter4;

import EDU.oswego.cs.dl.util.concurrent.Callable;

import java.lang.reflect.InvocationTargetException;

/**
 * @author lili
 * @date 2022/6/19 10:46
 */
public class FutureResult {                            // Fragments
    protected Object value = null;
    protected boolean ready = false;
    protected InvocationTargetException exception = null;

    public synchronized Object get()
            throws InterruptedException, InvocationTargetException {

        while (!ready) wait();

        if (exception != null)
            throw exception;
        else
            return value;
    }

    public Runnable setter(final Callable function) {
        return new Runnable() {
            public void run() {
                try {
                    set(function.call());
                } catch (Throwable e) {
                    setException(e);
                }
            }
        };
    }

    synchronized void set(Object result) {
        value = result;
        ready = true;
        notifyAll();
    }

    synchronized void setException(Throwable e) {
        exception = new InvocationTargetException(e);
        ready = true;
        notifyAll();
    }

    // ... other auxiliary and convenience methods ...

}
