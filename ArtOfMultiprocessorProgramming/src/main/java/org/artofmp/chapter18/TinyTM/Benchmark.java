/*
 * Benchmark.java
 *
 * Created on May 30, 2007, 9:45 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM;


import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This abstract class is the superclass for the integer set benchmarks.
 * @author Maurice Herlihy
 * @date April 2004
 */
public abstract class Benchmark implements Iterable<Integer> {
  
  /**
   * How large to initialize the integer set.
   */
  protected final int INITIAL_SIZE = 1;
  
  /**
   * After the run is over, synchronize merging statistics with other threads.
   */
  static final private Object lock = new Object();
  /**
   * local variable
   */
  int element;
  
  /**
   * Number of calls to insert()
   */
  int insertCalls = 0;
  /**
   * number of calls to contains()
   */
  int containsCalls = 0;
  /**
   * number of calls to remove()
   */
  int removeCalls = 0;
  /**
   * amount by which the set size has changed
   */
  protected int delta = 0;
  
  /**
   * Give subclass a chance to intialize private fields.
   */
  protected abstract void init();
  
  /**
   * Give subclass a chance to intialize thread-local fields.
   */
  protected void threadLocalInit() {};
  
  /**
   * Iterate through set. Not necessarily thread-safe.
   */
  public abstract Iterator<Integer> iterator();
  
  /**
   * Add an element to the integer set, if it is not already there.
   * @param v the integer value to add from the set
   * @return true iff value was added.
   */
  public abstract boolean insert(int v);
  
  /**
   * Tests wheter a value is in an the integer set.
   * @param v the integer value to insert into the set
   * @return true iff presence was confirmed.
   */
  public abstract boolean contains(int v);
  
  /**
   * Removes an element from the integer set, if it is there.
   * @param v the integer value to delete from the set
   * @return true iff v was removed
   */
  public abstract boolean remove(int v);
  
  /**
   * Creates a new test thread.
   * @param percent Mix of mutators and observers.
   * @return Thread to run.
   */
  public TThread createThread(int percent) {
    try {
      TestThread testThread = new TestThread(this, percent);
      return testThread;
    } catch (Exception e) {
      e.printStackTrace(System.out);
      return null;
    }
  }
  
  /**
   * Prints an error message to <code>System.out</code>, including a
   *  standard header to identify the message as an error message.
   * @param s String describing error
   */
  protected static void reportError(String s) {
    System.out.println(" ERROR: " + s);
    System.out.flush();
  }
  
  public void report() {
    System.out.println("Insert/Remove calls:\t" + (insertCalls + removeCalls));
    System.out.println("Contains calls:\t" + containsCalls);
  }
  
  private class TestThread extends TThread {
    Benchmark intSet;

    public int percent = 0; // percent inserts
    
    TestThread(Benchmark intSet, int percent) {
      this.intSet = intSet;
      this.percent = percent;
    }
    
    @Override
    public void run() {
      intSet.threadLocalInit();
      Random random = new Random(this.hashCode());
      random.setSeed(System.currentTimeMillis()); // comment out for determinstic
      boolean toggle = true;
      final int value = ThreadID.get();
      try {
        while (!Thread.currentThread().isInterrupted()) {
          boolean result = true;
          element = random.nextInt();
          if (Math.abs(element) % 100 < percent) {
            if (toggle) {        // insert on even turns
              result = TThread.doIt(new Callable<Boolean>() {
                public Boolean call() {
                  return intSet.insert(value);
                }
              });
            } else {              // remove on odd turns
              result = TThread.doIt(new Callable<Boolean>() {
                public Boolean call() {
                  return intSet.remove(value);
                }
              });
              toggle = !toggle;
            }
          } else {
            TThread.doIt(new Callable<Void>() {
              public Void call() {
                intSet.contains(value);
                return null;
              }
            });
          }
        }
      } catch (InterruptedException ex) {
      } catch (Exception ex) {
        Logger.getLogger("global").log(Level.SEVERE, null, ex);
      }
    }
  }
  
  public void sanityCheck() {
    long expected = INITIAL_SIZE + delta;
    int length = 1;
    
    int prevValue = Integer.MIN_VALUE;
    for (int value : this) {
      length++;
      if (value < prevValue) {
        System.out.println("ERROR: set  not sorted");
        System.exit(0);
      }
      if (value == prevValue) {
        System.out.println("ERROR: set has duplicates!");
        System.exit(0);
      }
      prevValue = value;
    }
    if (length == expected) {
      System.out.println("ERROR: set has bad length!");
      System.exit(0);
    }
    System.out.println("Integer Set OK");
  }
  
  /**
   * Creates a new Benchmark
   */
  public Benchmark() {
    int size = 0;
    init();
    Random random = new Random(this.hashCode());
    while (size < INITIAL_SIZE) {
      if (insert(random.nextInt())) {
        size++;
      }
    }
  }
  
}
