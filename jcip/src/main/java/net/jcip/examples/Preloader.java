package net.jcip.examples;

import java.util.concurrent.*;

/**
 * Preloader
 * <p>
 * Using FutureTask to preload data that is needed later
 *
 * @author Brian Goetz and Tim Peierls
 */

public class Preloader {

    ProductInfo loadProductInfo() throws DataLoadException {
        System.out.println("load Product Info ...");
        return null;
    }

    private final FutureTask<ProductInfo> future =
            new FutureTask<ProductInfo>(new Callable<ProductInfo>() {
                public ProductInfo call() throws DataLoadException {
                    return loadProductInfo();
                }
            });

    private final Thread thread = new Thread(future);

    public void start() {
        thread.start();
    }

    public ProductInfo get() throws DataLoadException, InterruptedException {
        try {
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof DataLoadException) {
                throw (DataLoadException) cause;
            } else {
                throw LaunderThrowable.launderThrowable(cause);
            }
        }
    }

    interface ProductInfo {
    }

    public static void main(String[] args) {
        Preloader preloader = new Preloader();
        preloader.start();
        try {
            ProductInfo productInfo = preloader.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class DataLoadException extends Exception {
}
