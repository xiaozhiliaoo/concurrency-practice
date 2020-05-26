/*
 * AtomicObject.java
 *
 * Created on January 17, 2007, 7:29 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM.ofree;

/**
 * Encapsulates transactional synchronization for obstruction-free objects.
 * @author Maurice Herlihy
 */

import org.artofmp.chapter18.TinyTM.AtomicObject;
import org.artofmp.chapter18.TinyTM.Copyable;
import org.artofmp.chapter18.TinyTM.Transaction;
import org.artofmp.chapter18.TinyTM.contention.ContentionManager;
import org.artofmp.chapter18.TinyTM.exceptions.AbortedException;
import org.artofmp.chapter18.TinyTM.exceptions.PanicException;

import java.util.concurrent.atomic.AtomicReference;
public class FreeObject<T extends Copyable<T>> extends AtomicObject<T> {
  AtomicReference<Locator> start;
  public FreeObject(T init) {
    super(init);
    start = new AtomicReference<Locator>(new Locator(init));
  }
  
  public T  openWrite() {
    Transaction me = Transaction.getLocal();
    switch (me.getStatus()) {
      case COMMITTED:
        return openSequential();
      case ABORTED:
        throw new AbortedException();
      case ACTIVE:
        Locator locator = start.get();
        if (locator.owner == me) {
          return locator.newVersion;
        }
        Locator newLocator = new Locator();
        while (!Thread.currentThread().isInterrupted()) {
          Locator oldLocator = start.get();
          Transaction writer = oldLocator.owner;
          switch (writer.getStatus()) {
            case COMMITTED:
              newLocator.oldVersion = oldLocator.newVersion;
              break;
            case ABORTED:
              newLocator.oldVersion = oldLocator.oldVersion;
              break;
            case ACTIVE:
              ContentionManager.getLocal().resolve(me, writer);
              continue;
          }
          try {
            newLocator.newVersion = myClass.newInstance();
          } catch (Exception ex) {
            throw new PanicException(ex);
          }
          newLocator.oldVersion.copyTo(newLocator.newVersion);
          if (start.compareAndSet(oldLocator, newLocator)) {
            return newLocator.newVersion;
          }
        }
        me.abort(); // time's up
        throw new AbortedException();
      default:
        throw new PanicException("Unexpected transaction state");
    }
  }
  private T openSequential() {
    Locator locator = start.get();
    switch (locator.owner.getStatus()) {
      case COMMITTED:
        return locator.newVersion;
      case ABORTED:
        return locator.oldVersion;
      default:
        throw new PanicException("Active/Inactitive transaction conflict");
    }
  }
  
  public T openRead() {
    Transaction me = Transaction.getLocal();
    switch (me.getStatus()) {
      case COMMITTED:
        return openSequential();
      case ABORTED:
        throw new AbortedException();
      case ACTIVE:
        Locator locator = start.get();
        if (locator.owner == me) {
          return locator.newVersion;
        }
        Locator newLocator = new Locator();
        while (!Thread.currentThread().isInterrupted()) {
          Locator oldLocator = start.get();
          Transaction writer = oldLocator.owner;
          switch (writer.getStatus()) {
            case COMMITTED:
              newLocator.oldVersion = oldLocator.newVersion;
              break;
            case ABORTED:
              newLocator.oldVersion = oldLocator.oldVersion;
              break;
            case ACTIVE:
              ContentionManager.getLocal().resolve(me, writer);
              continue;
          }
          if (start.compareAndSet(oldLocator, newLocator)) {
            return newLocator.newVersion;
          }
        }
        me.abort(); // time's up
        throw new AbortedException();
      default:
        throw new PanicException("Unexpected transaction state");
    }
  }
  
  public boolean validate() {
    switch (Transaction.getLocal().getStatus()) {
      case COMMITTED:
        return true;
      case ABORTED:
        return false;
      case ACTIVE:
        return true;
      default:
        throw new PanicException("Unexpected Transaction state");
    }
  }
  
  private class Locator {
    Transaction owner;
    T oldVersion;
    T newVersion;
    Locator() {
      owner = Transaction.COMMITTED;
    }
    Locator(T version) {
      this();
      newVersion = version;
    }
  }
  
}
