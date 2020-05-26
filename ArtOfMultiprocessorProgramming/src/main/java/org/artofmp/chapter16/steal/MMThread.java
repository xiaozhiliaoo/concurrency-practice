/*
 * MMThread.java
 *
 * Created on January 21, 2006, 10:37 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter16.steal;

/**
 * Matrix multiplication using threads.
 *
 * @author Maurice Herlihy
 */
class MMThread {
    double[][] a, b, c;
    int n;

    public MMThread(double[][] a, double[][] b) {
        n = a.length;
        this.a = a;
        this.b = b;
        this.c = new double[n][n];
    }

    void multiply() {
        Worker[][] worker = new Worker[n][n];
        // create one thread per matrix entry
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                worker[row][col] = new Worker(row, col);
            }
        }
        // start the threads
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                worker[row][col].start();
            }
        }
        // wait for them to finish
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                try {
                    worker[row][col].join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    class Worker extends Thread {
        int row, col;

        Worker(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public void run() {
            double dotProduct = 0.0;
            for (int i = 0; i < n; i++) {
                dotProduct += a[row][i] * b[i][col];
            }
            c[row][col] = dotProduct;
        }
    }
} 
