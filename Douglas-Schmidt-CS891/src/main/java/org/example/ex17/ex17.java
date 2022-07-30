package org.example.ex17;

import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * This example shows various issues associated with using the Java
 * stream reduce() terminal operation, including the need to use the
 * correct identity value and to ensure operations are associative.
 * It also demonstrates what goes wrong when reduce() performs a
 * mutable reduction on a parallel stream.
 */
public class ex17 {
    /**
     * Main entry point into the program.
     */
    public static void main(String[] argv) {
        // Run the difference reduction test sequentially.
        testDifferenceReduce(false);

        // Run the difference reduction test in parallel.
        testDifferenceReduce(true);

        // Run the summation reduction test sequentially with the
        // correct identity value.
        testSum(0L, false);

        // Run the summation reduction test in parallel with the
        // correct identity value.
        testSum(0L, true);

        // Run the summation reduction test sequentially with an
        // incorrect identity value.
        testSum(1L, false);

        // Run the summation reduction test in parallel with an
        // incorrect identity value.
        testSum(1L, true);

        // Run the product reduction test in parallel with an
        // incorrect identity value.
        testSum(0L, true);

        // Run the product reduction test sequentially with the
        // correct identity value.
        testProd(1L, false);

        // Run the product reduction test in parallel with the
        // correct identity value.
        testProd(1L, true);

        // Run the product reduction test sequentially with an
        // incorrect identity value.
        testProd(0L, false);

        // Reduce partial results into a string using a sequential
        // stream and the three parameter version of reduce() along
        // with StringBuilder.
        buggyStreamReduce3a(false);

        // Reduce partial results into a string using a parallel
        // stream and the three parameter version of reduce() along
        // with StringBuilder..
        buggyStreamReduce3a(true);

        // Reduce partial results into a string using a sequential
        // stream and the three parameter version of reduce() along
        // with StringBuffer.
        buggyStreamReduce3b(false);

        // Reduce partial results into a string using a parallel
        // stream and the three parameter version of reduce() along
        // with StringBuffer.
        buggyStreamReduce3b(true);

        // Reduce partial results into a string using a sequential
        // stream and string concatenation with reduce().
        streamReduceConcat(false);

        // Reduce partial results into a string using a parallel
        // stream and string concatenation with reduce().
        streamReduceConcat(true);

        // Collect partial results into a string using a sequential
        // stream together with collect() and StringJoiner.
        streamCollectJoining(false);

        // Collect partial results into a string using a parallel
        // stream together with collect() and StringJoiner.
        streamCollectJoining(true);
    }

    /**
     * Print out the results of subtracting the first 100 numbers.
     * If @a parallel is true then a parallel stream is used, else a
     * sequential stream is used.  The results for each of these tests
     * will differ since subtraction is not associative.
     */
    private static void testDifferenceReduce(boolean parallel) {
        LongStream rangeStream = LongStream
            .rangeClosed(1, 100);

        if (parallel)
            rangeStream.parallel();

        long difference = rangeStream
            .reduce(1L,
                    (x, y) -> x - y);

        System.out.println((parallel ? "Parallel" : "Sequential")
                           + " difference of first 100 numbers = "
                           + difference);
    }

    /**
     * Print out the results of summing the first 100 numbers,
     * using @a identity as the initial value of the summation.  If @a
     * parallel is true then a parallel stream is used, else a
     * sequential stream is used.  When a sequential or parallel
     * stream is used with an identity of 0 the results of this test
     * will be correct.  When a sequential or parallel stream is used
     * with an identity of 1, however, results of this test will be
     * incorrect.
     */
    private static void testSum(long identity,
                                boolean parallel) {
        LongStream rangeStream = LongStream
            .rangeClosed(1, 100);

        if (parallel)
            rangeStream.parallel();

        long sum = rangeStream
            .reduce(identity,
                    // Could also use (x, y) -> x + y
                    Math::addExact);

        System.out.println((parallel ? "Parallel" : "Sequential")
                           + " sum of first 100 numbers with identity "
                           + identity
                           + " = "
                           + sum);
    }

    /**
     * Print out the results of multiplying the first 100 numbers,
     * using @a identity as the initial value of the summation.  If @a
     * parallel is true then a parallel stream is used, else a
     * sequential stream is used.  When a sequential or parallel
     * stream is used with an identity of 1 the results of this test
     * will be correct.  When a sequential or parallel stream is used
     * with an identity of 0, however, results of this test will be
     * incorrect.
     */
    private static void testProd(long identity,
                                 boolean parallel) {
        LongStream rangeStream = LongStream
            .rangeClosed(1, 10);

        if (parallel)
            rangeStream.parallel();

        long product = rangeStream
            .reduce(identity,
                    (x, y) -> x * y);

        System.out.println((parallel ? "Parallel" : "Sequential")
                           + " product of first 10 numbers with identity "
                           + identity
                           + " = "
                           + product);
    }

    /**
     * Reduce partial results into a StringBuilder using the three
     * parameter version of reduce().  If @a parallel is true then a
     * parallel stream is used, else a sequential stream is used.
     * When a sequential stream is used the results of this test will
     * be correct even though a mutable object (StringBuilder) is used
     * with reduce().  When a parallel stream is used, however, the
     * results of this test will be incorrect due to the use of a
     * mutable object (StringBuilder) with reduce(), which expects
     * an immutable object.
     */
    private static void buggyStreamReduce3a(boolean parallel) {
        System.out.println("\n++Running the "
                           + (parallel ? "parallel" : "sequential")
                           + "buggyStreamReduce3 implementation");

        List<String> allStrings =
            List.of("The quick brown fox jumps over the lazy dog\n",
                    "A man, a plan, a canal: Panama\n",
                    "Now is the time for all good people\n",
                    "to come to the aid of their party\n");

        // Record the start time.
        long startTime = System.nanoTime();

        Stream<String> stringStream = allStrings
            // Convert the list into a stream (which uses a
            // spliterator internally).
            .stream();

        if (parallel)
            // Convert to a parallel stream.
            stringStream.parallel();

        // A "real" application would likely do something interesting
        // with the strings at this point.

        // Create a string that contains all the strings appended together.
        String reducedString = stringStream
            // Use reduce() to append all the strings in the stream.
            // This implementation will fail when used with a parallel
            // stream since reduce() expects to do "immutable"
            // reduction, but there's just a single StringBuilder!
            .reduce(new StringBuilder(),
                    StringBuilder::append,
                    StringBuilder::append)
            // Create a string.
            .toString();

        // Record the stop time.
        long stopTime = (System.nanoTime() - startTime) / 1_000_000;

        System.out.println("The time to collect "
                           + allStrings.size()
                           + " strings into "
                           + reducedString.split("\\n").length
                           + " strings took "
                           + stopTime
                           + " milliseconds.  Here are the strings:\n"
                           + reducedString);
    }

    /**
     * Reduce partial results into a StringBuilder using the three
     * parameter version of reduce().  If @a parallel is true then a
     * parallel stream is used, else a sequential stream is used.
     * When a sequential stream is used the results of this test will
     * be correct even though a mutable object (StringBuilder) is used
     * with reduce().  When a parallel stream is used, however, the
     * results of this test will be incorrect due to the use of a
     * mutable object (StringBuffer) with reduce(), which expects
     * an immutable object.
     */
    private static void buggyStreamReduce3b(boolean parallel) {
        System.out.println("\n++Running the "
                           + (parallel ? "parallel" : "sequential")
                           + "buggyStreamReduce3b implementation");

        List<String> allStrings =
            List.of("The quick brown fox jumps over the lazy dog\n",
                    "A man, a plan, a canal: Panama\n",
                    "Now is the time for all good people\n",
                    "to come to the aid of their party\n");

        // Record the start time.
        long startTime = System.nanoTime();

        Stream<String> stringStream = allStrings
            // Convert the list into a stream (which uses a
            // spliterator internally).
            .stream();

        if (parallel)
            // Convert to a parallel stream.
            stringStream.parallel();

        // A "real" application would likely do something interesting
        // with the strings at this point.

        // Create a string that contains all the strings appended together.
        String reducedString = stringStream
            // Use reduce() to append all the strings in the stream.
            // This implementation will fail when used with a parallel
            // stream since reduce() expects to do "immutable"
            // reduction, but there's just a single StringBuffer!
            .reduce(new StringBuffer(),
                    StringBuffer::append,
                    StringBuffer::append)
            // Create a string.
            .toString();

        // Record the stop time.
        long stopTime = (System.nanoTime() - startTime) / 1_000_000;

        System.out.println("The time to collect "
                           + allStrings.size()
                           + " strings into "
                           + reducedString.split("\\n").length
                           + " strings took "
                           + stopTime
                           + " milliseconds.  Here are the strings:\n"
                           + reducedString);
    }

    /**
     * Reduce partial results into a String using reduce() with string
     * concatenation (i.e., the '+' operator).  If @a parallel is
     * true then a parallel stream is used, else a sequential stream
     * is used.  This solution is correct, but inefficient due to the
     * overhead of string concatentation.
     */
    private static void streamReduceConcat(boolean parallel) {
        System.out.println("\n++Running the "
                           + (parallel ? "parallel" : "sequential")
                           + "streamReduceConcat implementation");

        List<String> allStrings =
            List.of("The quick brown fox jumps over the lazy dog\n",
                    "A man, a plan, a canal: Panama\n",
                    "Now is the time for all good people\n",
                    "to come to the aid of their party\n");

        // Record the start time.
        long startTime = System.nanoTime();

        Stream<String> stringStream = allStrings
            // Convert the list into a stream (which uses a
            // spliterator internally).
            .stream();

        if (parallel)
            // Convert to a parallel stream.
            stringStream.parallel();

        // A "real" application would likely do something interesting
        // with the strings at this point.

        // Create a string that contains all the strings appended
        // together.
        String reducedString = stringStream
            // Use reduce() to append all the strings in the stream.
            // This implementation works with both sequential and
            // parallel streams, but it's inefficient since it
            // requires string concatenation.
            .reduce("",
                    (x, y) -> x + y);


        // Record the stop time.
        long stopTime = (System.nanoTime() - startTime) / 1_000_000;

        System.out.println("The time to collect "
                           + allStrings.size()
                           + " strings into "
                           + reducedString.split("\\n").length
                           + " strings took "
                           + stopTime
                           + " milliseconds.  Here are the strings:\n"
                           + reducedString);
    }

    /**
     * Collect partial results into a string using a parallel stream
     * together with collect() and joining().  If @a parallel is
     * true then a parallel stream is used, else a sequential stream
     * is used.  When a sequential stream or a parallel stream is used
     * the results of this test will be correct due to the use of a
     * mutable object (StringJoiner) with collect(), which works
     * correctly in this case.
     */
    private static void streamCollectJoining(boolean parallel) {
        System.out.println("\n++Running the "
                           + (parallel ? "parallel" : "sequential")
                           + "streamCollectJoining implementation");

        List<String> allStrings =
            List.of("The quick brown fox jumps over the lazy dog\n",
                    "A man, a plan, a canal: Panama\n",
                    "Now is the time for all good people\n",
                    "to come to the aid of their party\n");

        // Record the start time.
        long startTime = System.nanoTime();

        Stream<String> stringStream = allStrings
            // Convert the list into a stream (which uses a
            // spliterator internally).
            .stream();

        if (parallel)
            // Convert to a parallel stream.
            stringStream.parallel();

        // A "real" application would likely do something interesting
        // with the strings at this point.

        // Create a string that contains all the strings appended
        // together.
        String reducedString = stringStream
            // Use collect() to append all the strings in the stream.
            // This implementation works when used with either a
            // sequential or a parallel stream.
            .collect(joining());

        // Record the stop time.
        long stopTime = (System.nanoTime() - startTime) / 1_000_000;

        System.out.println("The time to collect "
                           + allStrings.size()
                           + " strings into "
                           + reducedString.split("\\n").length
                           + " strings took"
                           + stopTime
                           + " milliseconds.  Here are the strings:\n"
                           + reducedString);
    }
}
