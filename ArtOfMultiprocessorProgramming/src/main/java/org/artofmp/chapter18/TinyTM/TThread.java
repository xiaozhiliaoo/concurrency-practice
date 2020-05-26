/*
 * TThread.java
 *
 * Created on January 3, 2007, 9:01 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM;


import org.artofmp.chapter18.TinyTM.exceptions.AbortedException;
import org.artofmp.chapter18.TinyTM.exceptions.PanicException;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class TThread extends Thread {
    static Runnable onStart = new DefaultRunnable();
    static Runnable onAbort = new DefaultRunnable();
    static Runnable onCommit = new DefaultRunnable();
    static Callable<Boolean> onValidate = new DefaultCallable();
    static public final AtomicInteger commits = new AtomicInteger(0);
    static public final AtomicInteger aborts = new AtomicInteger(0);

    public TThread() {

    }

    public static <T> T doIt(Callable<T> xaction) throws Exception {
        T result;
        Transaction me;
        Thread myThread = Thread.currentThread();
        Exception rethrow = null;
        while (!myThread.isInterrupted()) {
            me = new Transaction();
            Transaction.setLocal(me);
            onStart.run();
            try {
                result = xaction.call();
                if (onValidate.call() && me.commit()) {
                    commits.getAndIncrement();
                    onCommit.run();
                    return result;
                }
            } catch (AbortedException e) {
            } catch (InterruptedException e) {
                myThread.interrupt();
            } catch (Exception e) {
                throw new PanicException(e);
            }
            aborts.getAndIncrement();
            onAbort.run();
        }
        throw new InterruptedException();
    }

    public static void onStart(Runnable handler) {
        onStart = handler;
    }

    public static void onCommit(Runnable handler) {
        onCommit = handler;
    }

    public static void onAbort(Runnable handler) {
        onAbort = handler;
    }

    public static void onValidate(Callable<Boolean> handler) {
        onValidate = handler;
    }

    static class DefaultRunnable implements Runnable {
        public void run() {
        }
    }

    static class DefaultCallable implements Callable<Boolean> {
        public Boolean call() {
            return true;
        }
    }
}
