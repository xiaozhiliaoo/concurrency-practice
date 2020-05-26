/*
 * PanicException.java
 *
 * Created on October 29, 2005, 9:07 AM
 */
package org.artofmp.chapter12.combine;
/**
 * Thrown by an attempt to open a <code>TMObject</code> to indicate
 * that the current transaction cannot commit.
 **/
public class PanicException extends RuntimeException {
  /**
   * Creates a new <code>PanicException</code> instance with no detail message.
   */
  public PanicException() {
    super();
  }  
  /**
   * Creates a new <code>Panic</code> instance with the specified detail message.
   * @param msg the detail message.
   */
  public PanicException(String msg) {
    super(msg);
  }
}
