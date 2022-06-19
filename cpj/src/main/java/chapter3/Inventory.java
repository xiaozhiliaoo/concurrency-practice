package chapter3;

import java.util.Hashtable;

/**
 * @author lili
 * @date 2022/6/19 10:08
 */
public class Inventory {

    protected final Hashtable items = new Hashtable();
    protected final Hashtable suppliers = new Hashtable();

    // execution state tracking variables:

    protected int storing = 0;    // number of in-progress stores
    protected int retrieving = 0; // number of retrieves

    // ground actions:

    protected void doStore(String description, Object item,
                           String supplier) {
        items.put(description, item);
        suppliers.put(supplier, description);
    }

    protected Object doRetrieve(String description) {
        Object x = items.get(description);
        if (x != null)
            items.remove(description);
        return x;
    }

    public void store(String description,
                      Object item,
                      String supplier)
            throws InterruptedException {

        synchronized (this) {                    // Before-action
            while (retrieving != 0) // don't overlap with retrieves
                wait();
            ++storing;                           // record exec state
        }

        try {
            doStore(description, item, supplier); // Ground action
        } finally {                               // After-action
            synchronized (this) {                  // signal retrieves
                if (--storing == 0) // only necessary when hit zero
                    notifyAll();
            }
        }
    }

    public Object retrieve(String description)
            throws InterruptedException {

        synchronized (this) {                    // Before-action
            // wait until no stores or retrieves
            while (storing != 0 || retrieving != 0)
                wait();
            ++retrieving;
        }

        try {
            return doRetrieve(description);       // ground action
        } finally {
            synchronized (this) {                  // After-action
                if (--retrieving == 0)
                    notifyAll();
            }
        }
    }
}
