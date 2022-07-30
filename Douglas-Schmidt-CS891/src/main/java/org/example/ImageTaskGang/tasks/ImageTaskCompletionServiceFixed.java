package org.example.ImageTaskGang.tasks;

import org.example.ImageTaskGang.filters.Filter;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Customizes the {@link ImageTaskGang} to use the {@link
 * ExecutorCompletionService} in conjunction with a fixed-sized thread
 * pool to concurrently download a list of images from web servers,
 * apply image processing filters to each image, and store the results
 * in files that can be displayed to users via various means defined
 * by the context in which this class is used.
 *
 * This class implements the roles of the "Proactive Initiator" and
 * "Completion Handler" in the Proactor pattern and also plays the
 * role of the "Concrete Class" in the Template Method pattern.
 */
public class ImageTaskCompletionServiceFixed
       extends ImageTaskCompletionService {
    /**
     * Constructor initializes the superclass and fields.
     */
    public ImageTaskCompletionServiceFixed(Filter[] filters,
                                           List<List<URL>> urlLists) {
        // Initialize the super class.
        super(filters, 
              urlLists,
              "ImageTaskCompletionServiceFixed");

    }

    /**
     * Hook method that returns a fixed-size thread pool
     * implementation of the {@link Executor}.
     */
    @Override
    public Executor executorHook() {
        // Create and Executor with a fixed pool of threads.
        Executor executor = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // Prestart all the core threads.
        ((ThreadPoolExecutor) executor).prestartAllCoreThreads();

        // Return the thread-pool executor.
        return executor;
    }
}    
