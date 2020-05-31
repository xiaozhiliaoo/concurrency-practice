package chapter3;

import EDU.oswego.cs.dl.util.concurrent.ReadWriteLock;
import EDU.oswego.cs.dl.util.concurrent.WriterPreferenceReadWriteLock;

/**
 * @author lili
 * @date 2020/5/30 21:01
 * @description
 * @notes
 */
public class DataRepository {                           // code sketch

    protected final ReadWriteLock rw = new WriterPreferenceReadWriteLock();

    public void access() throws InterruptedException {
        rw.readLock().acquire();
        try {
            /* read data */
        } finally {
            rw.readLock().release();
        }
    }

    public void modify() throws InterruptedException {
        rw.writeLock().acquire();
        try {
            /* write data */
        } finally {
            rw.writeLock().release();
        }
    }

}
