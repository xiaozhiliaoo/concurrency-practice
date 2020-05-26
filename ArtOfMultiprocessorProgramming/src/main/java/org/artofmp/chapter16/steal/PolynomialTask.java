/*
 * PolynomialTask.java
 *
 * Created on December 10, 2006, 6:52 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter16.steal;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Parallel polynomial addition and multiplication.
 * @author Maurice Herlihy
 */
public class PolynomialTask {
  static ExecutorService exec = Executors.newCachedThreadPool();
  
  static class Polynomial {
    int[] coefficients;
    int first;    // index of my constant coefficient
    int degree;   // number of coefficients that are mine
    /**
     * @param d dimension of zero-filled matrix
     */
    Polynomial(int d) {
      coefficients = new int[d];
      degree = d;
      first = 0;
    }
    /**
     * @param coefficients backing array for polynomial
     * @param first index of constant coefficient
     * @param degree number of meaningful coefficients
     */
    Polynomial(int[] coefficients, int first, int degree) {
      this.coefficients = coefficients;
      this.first = first;
      this.degree = degree;
    }
    /**
     * return coefficient
     * @param index which coefficient
     * @return value of coefficient
     */
    int get(int index) {
      return coefficients[first + index];
    }
    /**
     * set coefficient
     * @param index which coefficient
     * @param value new value for coefficient
     */
    void set(int index, int value) {
      coefficients[first + index] = value;
    }
    /**
     * Degree of polynomial.
     * @return polynomial degree
     */
    int getDegree() {
      return degree;
    }
    Polynomial[] split() {
      Polynomial[] result = new Polynomial[2];
      int newDegree = degree / 2;
      result[0] = new Polynomial(coefficients, first, newDegree);
      result[1] = new Polynomial(coefficients, first + newDegree, newDegree);
      return result;
    }
    public String toString() {
      String result = "(" + degree + "):";
      Boolean first = true;
      // constant polynomial
      if (degree == 0) {
        return result + coefficients[0];
      }
      if (coefficients[0] != 0) {
        result += coefficients[0];
        first = false;
      }
      for (int i = 1; i < degree; i++) {
        if (coefficients[i] != 0) {
          if (first) {
            result += coef(i) + "x" + exp(i) + " ";
            first = false;
          } else {
            result += " + " + coef(i) + "x" + exp(i);
          }
        }
      }
      return result;
    }
    String coef(int i) {
      if (coefficients[i] == 1)
        return "";
      else
        return Integer.toString(coefficients[i]);
    }
    String exp(int e) {
      if (e == 1)
        return "";
      else
        return "^" + Integer.toString(e);
    }
  }
  static Polynomial add(Polynomial p, Polynomial q) throws InterruptedException, ExecutionException {
    int n = p.getDegree();
    Polynomial r = new Polynomial(n);
    Future<?> future = exec.submit(new AddTask(p, q, r));
    future.get();
    return r;
  }
  static Polynomial multiply(Polynomial p, Polynomial q) {
    int n = p.getDegree();
    Polynomial r = new Polynomial(2 * n);
    Future<?> future = exec.submit(new MulTask(p, q, r));
    try {
      future.get();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return r;
  }
  static class AddTask implements Runnable {
    Polynomial p, q, r;
    public AddTask(Polynomial p, Polynomial q, Polynomial r) {
      this.p = p; this.q = q; this.r = r;
    }
    public void run() {
      try {
        int n = p.getDegree();
        if (n == 1) {
          r.set(0, p.get(0) + q.get(0));
        } else {
          Polynomial[] pp = p.split(), qq = q.split(), rr = r.split();
          Future<?>[] future = (Future<?>[]) new Future[2];
          // create asynchronous computations
          future[0] = exec.submit(new AddTask(pp[0], qq[0], rr[0]));
          future[1] = exec.submit(new AddTask(pp[1], qq[1], rr[1]));
          // wait for them to finish
          future[0].get();
          future[1].get();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
  static class MulTask implements Runnable {
    Polynomial p, q, r;
    Polynomial[] temp;
    public MulTask(Polynomial p, Polynomial q, Polynomial r) {
      this.p = p; this.q = q; this.r = r;
      int newDegree = 2 * p.getDegree();
      temp = new Polynomial[6];
      for (int i  = 0; i < temp.length; i++) {
        temp[i] = new Polynomial(newDegree);
      }
    }
    public void run() {
      try {
        if (p.getDegree() == 0) {
          r.set(0, p.get(0) * q.get(0));
        } else {
          Polynomial[] pp = p.split();
          Polynomial[] qq = q.split();
          Future<?>[] future = (Future<?>[]) new Future[7];
          // launch parallel multiplications
          future[0] = exec.submit(new MulTask(pp[0], qq[0], temp[0]));
          future[1] = exec.submit(new MulTask(pp[0], qq[1], temp[1]));
          future[2] = exec.submit(new MulTask(pp[1], qq[0], temp[2]));
          future[3] = exec.submit(new MulTask(pp[1], qq[1], temp[3]));
          // wait for them to finish
          for (int i = 0; i < 4; i++)
            future[i].get();
          // do partial sums
          future[5] = exec.submit(new AddTask(temp[0], temp[1], temp[4]));
          future[6] = exec.submit(new AddTask(temp[2], temp[3], temp[5]));
          // wait for them to finish
          future[5].get();
          future[6].get();
          // final sum
          future[7] = exec.submit(new AddTask(temp[4], temp[5], r));
          future[7].get();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
  
}
