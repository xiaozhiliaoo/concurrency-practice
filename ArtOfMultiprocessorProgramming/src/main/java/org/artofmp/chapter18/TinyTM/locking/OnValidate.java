/*
 * OnValidate.java
 *
 * Created on January 14, 2007, 6:45 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM.locking;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Call this code when a transaction must decide whether it can commit.
 * @author Maurice Herlihy
 */
public class OnValidate implements Callable<Boolean>{
  private static final long TIMEOUT = 1024;
  
  public Boolean call() throws Exception {
    WriteSet writeSet = WriteSet.getLocal();
    ReadSet readSet  = ReadSet.getLocal();
    if (!writeSet.tryLock(TIMEOUT, TimeUnit.MILLISECONDS)) {
      return false;
    }
    for (LockObject x : readSet) {
      if (x.lock.isLocked() && !x.lock.isHeldByCurrentThread())
        return false;
      if (!x.validate()) {
        return false;
      }
    }
    return true;
  }
}
