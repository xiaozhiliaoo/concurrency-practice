/*
 * ArraySumTest.java
 * JUnit based test
 *
 * Created on December 9, 2006, 5:48 PM
 */

package org.artofmp.chapter16.steal;

import junit.framework.*;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

/**
 * @author mph
 */
public class ArraySumTest {

    @Test
    public void testRun() throws InterruptedException, ExecutionException {
        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3, 4};
        int sum = ArraySum.sum(a);
        assertEquals(6, sum);
        sum = ArraySum.sum(b);
        assertEquals(10, sum);
    }
}
