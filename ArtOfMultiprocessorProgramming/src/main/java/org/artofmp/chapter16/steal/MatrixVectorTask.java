/*
 * MatrixVectorTask.java
 *
 * Created on December 15, 2006, 9:41 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter16.steal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Parallel Matrix-Vector Multiplication
 * Exercise solution
 * @author Maurice Herlihy
 */
public class MatrixVectorTask {
  static ExecutorService exec = Executors.newCachedThreadPool();
  
  private static class Matrix {
    int dim;
    double[][] data;
    int rowDisplace;
    int colDisplace;
    /**
     * constructor
     * @param d dimension of zero-filled matrix
     */
    Matrix(int d) {
      dim = d;
      rowDisplace = colDisplace = 0;
      data = new double[d][d];
    }
    /**
     * constructor
     * @param matrix backing array for matrix
     * @param x offset of x origin
     * @param y offset of y origin
     * @param d dimension
     */
    Matrix(double[][] matrix, int x, int y, int d) {
      data = matrix;
      rowDisplace = x;
      colDisplace = y;
      dim  = d;
    }
    /**
     * return value
     * @param row coordinate
     * @param col coordinate
     * @return value at coordinate
     */
    double get(int row, int col) {
      return data[row+rowDisplace][col+colDisplace];
    }
    /**
     * set value at coordinate
     * @param row coordinate
     * @param col coordinate
     * @param value new value for position
     */
    void set(int row, int col, double value) {
      data[row+rowDisplace][col+colDisplace] = value;
    }
    /**
     * @return matrix dimension
     **/
    int getDim() {
      return dim;
    }
    /**
     * @return array of half-size matrices, backed by original.
     **/
    Matrix[][] split() {
      Matrix[][] result = new Matrix[2][2];
      int newDim = dim / 2;
      result[0][0] = new Matrix(data, rowDisplace, colDisplace, newDim);
      result[0][1] = new Matrix(data, rowDisplace, colDisplace + newDim, newDim);
      result[1][0] = new Matrix(data, rowDisplace + newDim, colDisplace, newDim);
      result[1][1] = new Matrix(data, rowDisplace + newDim, colDisplace + newDim, newDim);
      return result;
    }
  }
  
  static Matrix multiply(Matrix a, Matrix b) throws InterruptedException, ExecutionException {
    int n = a.getDim();
    Matrix c = new Matrix(n);
    Future<?> future = exec.submit(new MulTask(a, b, c));
    future.get();
    return c;
  }
  static class MulTask implements Runnable {
    Matrix a, b, c, lhs, rhs;
    public MulTask(Matrix a, Matrix b, Matrix c) {
      this.a = a; this.b = b; this.c = c;
      this.lhs = new Matrix(a.getDim());
      this.rhs = new Matrix(a.getDim());
    }
    public void run() {
      try {
        if (a.getDim() == 1) {
          c.set(0, 0, a.get(0,0) * b.get(0,0));
        } else {
          Matrix[][] aa = a.split(), bb = b.split(), cc = c.split();
          Matrix[][] ll = lhs.split(), rr = rhs.split();
          Future<?>[][][] future = (Future<?>[][][]) new Future[2][2][2];
          // launch parallel multiplications
          for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++) {
            future[i][j][0] = exec.submit(new MulTask(aa[i][0], bb[0][i], ll[i][j]));
            future[i][j][1] = exec.submit(new MulTask(aa[1][i], bb[i][1], rr[i][j]));
            }
          // wait for them to finish
          for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
              for (int k = 0; k < 2; k++)
                future[i][j][k].get();
          // do sum
          Future<?> done = exec.submit(new AddTask(lhs, rhs, c));
          done.get();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
  static class AddTask implements Runnable {
    Matrix a, b, c;
    public AddTask(Matrix a, Matrix b, Matrix c) {
      this.a = a; this.b = b; this.c = c;
    }
    public void run() {
      try {
        int n = a.getDim();
        if (n == 1) {
          c.set(0, 0, a.get(0,0) + b.get(0,0));
        } else {
          Matrix[][] aa = a.split(), bb = b.split(), cc = c.split();
          Future<?>[][] future = (Future<?>[][]) new Future[2][2];
          // create asynchronous computations
          for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
              future[i][j] = exec.submit(new AddTask(aa[i][j], bb[i][j], cc[i][j]));
          // wait for them to finish
          for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
              future[i][j].get();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}

