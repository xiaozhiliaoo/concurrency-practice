/*
 * OnCommit.java
 *
 * Created on January 14, 2007, 8:52 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM.locking;

import org.artofmp.chapter18.TinyTM.Copyable;

import java.util.Map;

/**
 * Handler for transaction commit.
 *
 * @author Maurice Herlihy
 */
public class OnCommit implements Runnable {
    @SuppressWarnings("unchecked")
    public void run() {
        WriteSet writeSet = WriteSet.getLocal();
        ReadSet readSet = ReadSet.getLocal();
        VersionClock.setWriteStamp();
        long writeVersion = VersionClock.getWriteStamp();
        for (Map.Entry<LockObject<?>, Object> entry : writeSet) {
            @SuppressWarnings("unchecked")
            LockObject<?> key = (LockObject<?>) entry.getKey();
            Copyable destin = (Copyable) key.openRead();
            Copyable source = (Copyable) entry.getValue();
            source.copyTo(destin);
            key.stamp = writeVersion;
        }
        writeSet.unlock();
        writeSet.clear();
        readSet.clear();
    }

}
