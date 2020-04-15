/*
 * Exchanger.java
 *
 * Created on May 19, 2007, 10:53 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2007 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter10.queue;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * Lock-free Exchanger.
 * Based on  * "A Scalable Elimination-based Exchange Channel" by W. Scherer,
 * D. Lea, and M. Scott.  In Proceedings of SCOOL05 workshop.
 * @author Maurice Herlihy
 */
public class Exchanger<V> {
  private static final int SIZE =
      (Runtime.getRuntime().availableProcessors() + 1) / 2;
  private static final long BACKOFF_BASE = 128L;
  static final Object FAIL = new Object();
  private final AtomicReference[] arena;
  private final Random random;
  public Exchanger() {
    arena = new AtomicReference[SIZE + 1];
    random = new Random();
    for (int i = 0; i < arena.length; ++i)
      arena[i] = new AtomicReference();
  }
  public V exchange(V x) throws InterruptedException {
    try {
      return (V)doExchange(x, Integer.MAX_VALUE);
    } catch (TimeoutException cannotHappen) {
      throw new Error(cannotHappen);
    }
  }
  public V exchange(V x, long timeout, TimeUnit unit)
  throws InterruptedException, TimeoutException {
    return (V)doExchange(x, unit.toNanos(timeout));
  }
  private Object doExchange(Object item, long nanos)
      throws InterruptedException, TimeoutException {
    Node me = new Node(item);
    long lastTime = System.nanoTime();
    int idx = 0;
    int backoff = 0;
    while (true) {
      AtomicReference<Node> slot = (AtomicReference<Node>)arena[idx];
      // If this slot is occupied, an item is waiting...
      Node you = slot.get();
      if (you != null) {
        Object v = you.fillHole(item);
        slot.compareAndSet(you, null);
        if (v != FAIL) // ... unless it's cancelled
          return v;
      }
      // Try to occupy this slot
      if (slot.compareAndSet(null, me)) {
        Object v = ((idx == 0)?
          me.waitForHole(nanos) :
          me.waitForHole(randomDelay(backoff)));
        slot.compareAndSet(me, null);
        if (v != FAIL)
          return v;
        if (Thread.interrupted())
          throw new InterruptedException();
        long now = System.nanoTime();
        nanos -= now - lastTime;
        lastTime = now;
        if (nanos <= 0)
          throw new TimeoutException();
        me = new Node(item);
        if (backoff < SIZE - 1)
          ++backoff;
        idx = 0; // Restart at top
      } else // Retry with a random non-top slot <= backoff
        idx = 1 + random.nextInt(backoff + 1);
    }
  }
  private long randomDelay(int backoff) {
    return ((BACKOFF_BASE << backoff) - 1) &
        random.nextInt();
  }
  static final class Node extends AtomicReference<Object> {
    final Object item;
    final Thread waiter;
    Node(Object item) {
      this.item = item;
      waiter = Thread.currentThread();
    }
    Object fillHole(Object val) {
      if (compareAndSet(null, val)) {
        LockSupport.unpark(waiter);
        return item;
      }
      return FAIL;
    }
      
    Object waitForHole(long nanos) {
      long lastTime = System.nanoTime();
      Object h;
      while ((h = get()) == null) {
        // If interrupted or timed out, try to
        // cancel by CASing FAIL as hole value.
        if (Thread.currentThread().isInterrupted() || nanos <= 0) {
          compareAndSet(null, FAIL);
        } else {
          LockSupport.parkNanos(nanos);
          long now = System.nanoTime();
          nanos -= now - lastTime;
          lastTime = now;
        }
      }
      return h;
    }
  }
}