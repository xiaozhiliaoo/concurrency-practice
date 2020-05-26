package org.artofmp.chapter18.TinyTM.exceptions;
/**
 * Thrown when a thread realizes that the current transaction cannot commit. 
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 **/
public class AbortedException extends RuntimeException {
  public AbortedException() {
    super();
  }
  public AbortedException(String msg) {
    super(msg);
  }
}
