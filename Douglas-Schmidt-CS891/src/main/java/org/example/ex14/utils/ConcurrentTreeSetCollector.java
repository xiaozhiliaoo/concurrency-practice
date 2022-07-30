package org.example.ex14.utils;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * A concurrent collector that accumulates input elements of type T
 * into a {@link ConcurrentHashSet} and the returns a {@link
 * TreeSet}.
 */
public class ConcurrentTreeSetCollector<T>
       implements Collector<T,
                            ConcurrentHashSet<T>,
                            TreeSet<T>> {
    /**
     * A factory method that creates and returns a new mutable result
     * container of type {@link ConcurrentHashSet} that holds all the
     * elements in the stream.
     *
     * @return a function which returns a new, mutable result container
     */
    @Override
    public Supplier<ConcurrentHashSet<T>> supplier() {
        return ConcurrentHashSet::new;
    }

    /**
     * A method that folds an element into the {@link TreeSet}.
     *
     * @return a function which folds a value into a mutable result container
     */
    @Override
    public BiConsumer<ConcurrentHashSet<T>, T> accumulator() {
        return ConcurrentHashSet::add;
    }

    /**
     * A method that accepts two partial results and merges them.
     *
     * @return null since this method is a no-op for a CONCURRENT
     * collector
     */
    @Override
    public BinaryOperator<ConcurrentHashSet<T>> combiner() {
        // This method is a no-op.
        return null;
    }

    /**
     * Perform the final transformation from the intermediate
     * accumulation type {@code A} to the final result type {@code
     * R}.
     *
     * @return A {@link TreeSet} containing the contents of the stream
     */
    @Override
    public Function<ConcurrentHashSet<T>, TreeSet<T>> finisher() {
        return c -> {
            TreeSet<T> set = new TreeSet<T>(c);
            System.out.println("Size of set = " + set.size() + " " + set);
            return set;
        };
    }

    /**
     * Returns a {@code Set} of {@code Collector.Characteristics}
     * indicating the characteristics of this Collector.  This set
     * should be immutable.
     *
     * @return An immutable set of collector characteristics, which in
     * this case is [CONCURRENT].
     */
    @SuppressWarnings("unchecked")
    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.CONCURRENT));
    }

    /**
     * This static factory method creates a new ConcurrentTreeSetCollector.
     *
     * @return A new ConcurrentTreeSetCollector()
     */
    public static <E> Collector<E, ?, TreeSet<E>> toSet() {
        return new ConcurrentTreeSetCollector<>();
    }
}


