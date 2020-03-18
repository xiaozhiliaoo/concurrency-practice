/*
  File: WaitableInt.java

  Originally written by Doug Lea and released into the public domain.
  This may be used for any purposes whatsoever without acknowledgment.
  Thanks for the assistance and support of Sun Microsystems Labs,
  and everyone contributing, testing, and using this code.

  History:
  Date       Who                What
  23Jun1998  dl               Create public version
  13may2004  dl               Add notifying bit ops
*/

package EDU.oswego.cs.dl.util.concurrent;

/**
 * A class useful for offloading waiting and signalling operations
 * on single int variables. 
 * <p>
 * <p>[<a href="http://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/intro.html"> Introduction to this package. </a>]
 **/

public class WaitableInt extends SynchronizedInt {
  /** 
   * Make a new WaitableInt with the given initial value,
   * and using its own internal lock.
   **/
  public WaitableInt(int initialValue) { 
    super(initialValue); 
  }

  /** 
   * Make a new WaitableInt with the given initial value,
   * and using the supplied lock.
   **/
  public WaitableInt(int initialValue, Object lock) { 
    super(initialValue, lock); 
  }


  public int set(int newValue) { 
    synchronized (lock_) {
      lock_.notifyAll();
      return super.set(newValue);
    }
  }

  public boolean commit(int assumedValue, int newValue) {
    synchronized (lock_) {
      boolean success = super.commit(assumedValue, newValue);
      if (success) lock_.notifyAll();
      return success;
    }
  }

  public int increment() { 
    synchronized (lock_) {
      lock_.notifyAll();
      return super.increment();
    }
  }

  public int decrement() { 
    synchronized (lock_) {
      lock_.notifyAll();
      return super.decrement();
    }
  }

  public int add(int amount) { 
    synchronized (lock_) {
      lock_.notifyAll();
      return super.add(amount);
    }
  }

  public int subtract(int amount) { 
    synchronized (lock_) {
      lock_.notifyAll();
      return super.subtract(amount);
    }
  }

  public int multiply(int factor) { 
    synchronized (lock_) {
      lock_.notifyAll();
      return super.multiply(factor);
    }
  }

  public int divide(int factor) { 
    synchronized (lock_) {
      lock_.notifyAll();
      return super.divide(factor);
    }
  }

  /** 
   * Set the value to its complement
   * @return the new value 
   **/
  public  int complement() { 
    synchronized (lock_) {
      value_ = ~value_;
      lock_.notifyAll();
      return value_;
    }
  }

  /** 
   * Set value to value &amp; b.
   * @return the new value 
   **/
  public  int and(int b) { 
    synchronized (lock_) {
      value_ = value_ & b;
      lock_.notifyAll();
      return value_;
    }
  }

  /** 
   * Set value to value | b.
   * @return the new value 
   **/
  public  int or(int b) { 
    synchronized (lock_) {
      value_ = value_ | b;
      lock_.notifyAll();
      return value_;
    }
  }


  /** 
   * Set value to value ^ b.
   * @return the new value 
   **/
  public  int xor(int b) { 
    synchronized (lock_) {
      value_ = value_ ^ b;
      lock_.notifyAll();
      return value_;
    }
  }


  /**
   * Wait until value equals c, then run action if nonnull.
   * The action is run with the synchronization lock held.
   **/

  public void whenEqual(int c, Runnable action) throws InterruptedException {
    synchronized(lock_) {
      while (!(value_ == c)) lock_.wait();
      if (action != null) action.run();
    }
  }

  /**
   * wait until value not equal to c, then run action if nonnull.
   * The action is run with the synchronization lock held.
   **/
  public void whenNotEqual(int c, Runnable action) throws InterruptedException {
    synchronized (lock_) {
      while (!(value_ != c)) lock_.wait();
      if (action != null) action.run();
    }
  }

  /**
   * wait until value less than or equal to c, then run action if nonnull.
   * The action is run with the synchronization lock held.
   **/
  public void whenLessEqual(int c, Runnable action) throws InterruptedException {
    synchronized (lock_) {
      while (!(value_ <= c)) lock_.wait();
      if (action != null) action.run();
    }
  }

  /**
   * wait until value less than c, then run action if nonnull.
   * The action is run with the synchronization lock held.
   **/
  public void whenLess(int c, Runnable action) throws InterruptedException {
    synchronized (lock_) {
      while (!(value_ < c)) lock_.wait();
      if (action != null) action.run();
    }
  }

  /**
   * wait until value greater than or equal to c, then run action if nonnull.
   * The action is run with the synchronization lock held.
   **/
  public void whenGreaterEqual(int c, Runnable action) throws InterruptedException {
    synchronized (lock_) {
      while (!(value_ >= c)) lock_.wait();
      if (action != null) action.run();
    }
  }

  /**
   * wait until value greater than c, then run action if nonnull.
   * The action is run with the synchronization lock held.
   **/
  public void whenGreater(int c, Runnable action) throws InterruptedException {
    synchronized (lock_) {
      while (!(value_ > c)) lock_.wait();
      if (action != null) action.run();
    }
  }

}

