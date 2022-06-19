package chapter3;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author lili
 * @date 2022/6/19 10:11
 */
public class Pool {                                     // Incomplete

    protected java.util.ArrayList items = new ArrayList();
    protected java.util.HashSet busy = new HashSet();

    protected final Semaphore available;

    public Pool(int n) {
        available = new Semaphore(n);
        initializeItems(n);
    }

    public Object getItem() throws InterruptedException {
        available.acquire();
        return doGet();
    }

    public void returnItem(Object x) {
        if (doReturn(x))
            available.release();
    }

    protected synchronized Object doGet() {
        Object x = items.remove(items.size() - 1);
        busy.add(x); // put in set to check returns
        return x;
    }

    protected synchronized boolean doReturn(Object x) {
        if (busy.remove(x)) {
            items.add(x); // put back into available item list
            return true;
        } else return false;
    }

    protected void initializeItems(int n) {
        // Somehow create the resource objects
        //   and place them in items list.
    }
}
