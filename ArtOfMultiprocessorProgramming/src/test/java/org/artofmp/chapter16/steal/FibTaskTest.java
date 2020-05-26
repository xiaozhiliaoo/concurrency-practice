/*
 * FibTest.java
 * JUnit based test
 *
 * Created on January 21, 2006, 5:47 PM
 */

package org.artofmp.chapter16.steal;

import java.util.concurrent.ExecutionException;

import junit.framework.*;

/**
 * @author mph
 */
public class FibTaskTest extends TestCase {

    public FibTaskTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(FibTaskTest.class);

        return suite;
    }

    /**
     * Test of run method, of class steal.FibTask.
     */
    public void testRun() {
        System.out.println("run");
        int result = 0;
        FibTask instance = new FibTask(16);
        result = instance.call();

        // TODO add your test code below by replacing the default call to fail.
        assertEquals(987, result);
    }

}
