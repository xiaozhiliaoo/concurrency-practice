package org.example.ex22;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.example.ex22.ExceptionUtils.rethrowFunction;

/**
 * A Java utility class that defines useful helper methods for
 * fork-join operations.
 */
public class ForkJoinUtils {
    private ForkJoinUtils() {}



    /**
     * Apply {@code op} to all items in the {@code list} using
     * iterative calls to fork-join methods.
     */
    public static <T> List<T> applyAllIter(List<T> list,
                                           Function<T, T> op,
                                           ForkJoinPool forkJoinPool) {
        // Invoke a new task in the fork-join pool.
        RecursiveTask<List<T>> task = new RecursiveTask<List<T>>() {
            /**
             * Entry point into the new task.
             */
            protected List<T> compute() {
                // Create a list to hold the forked tasks.
                List<ForkJoinTask<T>> forks = new LinkedList<>();

                // Create a list to hold the joined results.
                List<T> results = new LinkedList<>();

                // Iterate through list, fork all the tasks,
                // and add them to the forks list.
                for (T t : list) {
                    // Add each new task to the forks list.
                    RecursiveTask<T> recursiveTask = new RecursiveTask<T>() {
                        /**
                         * Apply the operation.
                         */
                        protected T compute() {
                            return op.apply(t);
                        }
                    };
                    forks.add(recursiveTask.fork());
                            // Fork a new task.
                }

                // Join all the results of the forked tasks.
                for (ForkJoinTask<T> task : forks) {
                    // Add the joined results.
                    results.add(task.join());
                }
                // Return the results.
                return results;
            }
        };
        return forkJoinPool.invoke(task);
    }

    /**
     * Apply {@code op} to all items in the {@code list} by
     * recursively splitting up calls to fork-join methods.
     */
    public static <T> List<T> applyAllSplitIndex(List<T> list,
                                                 Function<T, T> op,
                                                 ForkJoinPool forkJoinPool) {
        // Create a new array to hold the results.
        T[] results = (T[]) Array.newInstance(list.get(0).getClass(), list.size());

        /**
         * This task partitions list recursively and runs each half in
         * a ForkJoinTask.  It uses indices to avoid the overhead of
         * copying.
         */
        class SplitterTask extends RecursiveAction {
            /**
             * The lo index in this partition.
             */
            private int mLo;

            /**
             * The hi index in this partition.
             */
            private int mHi;

            /**
             * Constructor initializes the fields.
             */
            private SplitterTask(int lo, int hi) {
                mLo = lo;
                mHi = hi;
            }

            /**
             * Recursively perform the computations in parallel using
             * the fork-join pool.
             */
            protected void compute() {
                // Find the midpoint.
                int mid = (mLo + mHi) >>> 1;

                // If there's just a single element then apply
                // the operation.
                if (mLo == mid) {
                    // Update the mLo location with the results of
                    // applying the operation.
                    results[mLo] = op.apply(list.get(mLo));
                } else {
                    // Create a new SplitterTask to handle the
                    // left-hand side of the list and fork it.
                    ForkJoinTask<Void> leftTask = new SplitterTask(mLo, mLo = mid).fork();

                    // Compute the right-hand side in parallel with
                    // the left-hand side.
                    compute();
                    
                    // Join with the left-hand side.  This is a
                    // synchronization point.
                    leftTask.join();
                }
            }
        }

        // Invoke a new SplitterTask in the fork-join pool.
        forkJoinPool.invoke(new SplitterTask(0, list.size()));

        // Create a list from the array of results and return it.
        return Arrays.asList(results);
    }

    /**
     * Apply {@code op} to all items in the {@code list} by
     * recursively splitting up calls to fork-join methods.
     */
    public static <T> void applyAllSplitIndexEx(List<T> list,
                                                Function<T, T> op,
                                                ForkJoinPool forkJoinPool,
                                                T[] results) {
        /**
         * This task partitions list recursively and runs each half in
         * a ForkJoinTask.  It uses indices to avoid the overhead of
         * copying.
         */
        class SplitterTask extends RecursiveAction {
            /**
             * The lo index in this partition.
             */
            private int mLo;

            /**
             * The hi index in this partition.
             */
            private int mHi;

            /**
             * Constructor initializes the fields.
             */
            private SplitterTask(int lo, int hi) {
                mLo = lo;
                mHi = hi;
            }

            /**
             * Recursively perform the computations in parallel using
             * the fork-join pool.
             */
            protected void compute() {
                // Find the midpoint.
                int mid = (mLo + mHi) >>> 1;

                // If there's just a single element then apply
                // the operation.
                if (mLo == mid) {
                    // Update the mLo location with the results of
                    // applying the operation.
                    results[mLo] = op.apply(list.get(mLo));
                } else {
                    // Create a new SplitterTask to handle the
                    // left-hand side of the list and fork it.
                    ForkJoinTask<Void> leftTask =
                            new SplitterTask(mLo, mLo = mid)
                                    .fork();

                    // Compute the right-hand side in parallel with
                    // the left-hand side.
                    compute();

                    // Join with the left-hand side.  This is a
                    // synchronization point.
                    leftTask.join();
                }
            }
        }

        // Invoke a new SplitterTask in the fork-join pool.
        forkJoinPool.invoke(new SplitterTask(0, list.size()));
    }

    /**
     * Apply {@code op} to all items in the {@code list} by
     * recursively splitting up calls to fork-join methods.
     */
    public static <T> List<T> applyAllSplit(List<T> list,
                                            Function<T, T> op,
                                            ForkJoinPool forkJoinPool) {
        /**
         * This task splits up the list recursively and runs
         * each half in a ForkJoinTask.
         */
        class SplitterTask extends RecursiveTask<List<T>> {
            /**
             * A reference to a portion of the original list.
             */
            private List<T> mList;

            /**
             * Constructor initializes the field.
             */
            private SplitterTask(List<T> list) {
                mList = list;
            }

            /**
             * Recursively perform the computations in parallel using
             * the fork-join pool.
             */
            protected List<T> compute() {
                // The base case for the recursion.
                if (mList.size() <= 1) {
                    // Create a new list to hold the result (if any).
                    List<T> result = new ArrayList<>();

                    // Iterate through the list.
                    for (T t : mList)
                        // Apply the operation and add the result to
                        // the result list.
                        result.add(op.apply(t));

                    // Return the result list.
                    return result;
                } else {
                    // Determine the midpoint of the list.
                    int mid = mList.size() / 2;

                    // Create a new SplitterTask to handle the
                    // left-hand side of the list and fork it.
                    ForkJoinTask<List<T>> leftTask = new SplitterTask(mList.subList(0, mid)).fork();
                                         
                    // Update mList to handle the right-hand side of
                    // the list.
                    mList = mList.subList(mid, mList.size());

                    // Compute the right-hand side.
                    List<T> rightResult = compute();
                    
                    // Join the left-hand side results.
                    List<T> leftResult = leftTask.join();

                    // Combine the left-hand and the right-hand side
                    // results.
                    leftResult.addAll(rightResult);
                    
                    // Return the joined results.
                    return leftResult;
                }
            }
        }

        // Invoke a new SpliterTask in the fork-join pool.
        return forkJoinPool.invoke(new SplitterTask(list));
    }

    /**
     * Apply {@code op} to all items in the {@code list} using the
     * fork-join pool invokeAll() method.
     * 不是forkjoin的task client
     */
    public static <T> List<T> invokeAll(List<T> list,
                                        Function<T, T> op,
                                        ForkJoinPool forkJoinPool) {
        // Create a new list of callables.
        List<Callable<T>> tasks = new ArrayList<>();

        // Add all the ops to the list.
        for (T t : list)
            tasks.add(() -> op.apply(t));


        // Create a list of elements from the list of futures and
        // return it.
        return forkJoinPool
                // Call invokeAll() to process all elements in the list.
                .invokeAll(tasks)

                // Convert the list of futures to a stream.
                .stream()

                // Map the futures to elements.
                .map(rethrowFunction(Future::get))

                // Collect the results into a list.
                .collect(toList());
    }

    /**
     * Apply {@code op} to all items in the {@code list} using the
     * parallel stream framework.
     */
    public static <T> List<T> applyParallelStream(List<T> list,
                                                  Function<T, T> op) {
        return list
                 // Convert the list to a parallel stream.
                 .parallelStream()

                 // Apply the op function to each element of the stream.
                 .map(op)

                 // Convert the transformed stream back into a list.
                 .collect(toList());
    }
}
