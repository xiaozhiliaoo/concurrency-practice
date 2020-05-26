/*
 * MatrixTaskTest.java
 * JUnit based test
 *
 * Created on January 22, 2006, 2:47 PM
 */

package org.artofmp.chapter16.steal;

import junit.framework.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author mph
 */
public class MatrixTaskTest extends TestCase {
  
  public MatrixTaskTest(String testName) {
    super(testName);
  }
  
  public static Test suite() {
    TestSuite suite = new TestSuite(MatrixTaskTest.class);
    
    return suite;
  }
  
  /**
   * Test of run method, of class steal.MatrixTask.
   */
  /*public void testRun() throws InterruptedException, ExecutionException {
    System.out.println("run");
    
    double[][] a = {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
    double[][] b = {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
    Matrix aa = new Matrix(a);
    Matrix bb = new Matrix(b);
    Matrix cc = MatrixTask.multiply(aa, bb);
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (i == j) {
          assertEquals(1.0, aa.get(i,i));
        } else {
          assertEquals(0.0, aa.get(i,j));
        }
      }
    }
  }*/
}
