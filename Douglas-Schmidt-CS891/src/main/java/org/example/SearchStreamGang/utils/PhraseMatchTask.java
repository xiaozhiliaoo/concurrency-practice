package org.example.SearchStreamGang.utils;

import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/**
 * This class is used in conjunction with the Java 7 fork-join pool to
 * create a list of SearchResults.Result objects that match the number
 * of times a phrase appears in an input string.  The comparison is
 * case-insensitive.  This version uses Java 8 streams to implement
 * the class logic more concisely.
 */
public class PhraseMatchTask
       extends RecursiveTask<List<SearchResults.Result>> {
    /**
     * The input string.
     */
    private CharSequence mInput;

    /**
     * The phrase to search for in the input string.
     */
    private final String mPhrase;

    /**
     * The compiled regular expression pattern.
     */
    private final Pattern mPattern;

    /**
     * The phrase matcher.
     */
    private Matcher mPhraseMatcher;

    /**
     * The minimum size of an input string to split.
     */
    private final int mMinSplitSize;

    /**
     * Keeps track of the offset needed to return the appropriate
     * index into the original string.
     */
    private int mOffset = 0;

    /**
     * Constructor initializes the fields.
     */
    public PhraseMatchTask(CharSequence input,
                           String phrase) {
        // Transform the phrase parameter to a regular expression.
        mPhrase = phrase;
        
        // Create a regex that will match the phrase across lines.
        String regexPhrase = 
            // Start with a word boundary.
            "\\b"
            + phrase
            // Remove leading/trailing whitespace.
            .trim()
            // Replace multiple spaces with one whitespace boundary
            // expression and delimit words.
            .replaceAll("\\s+", "\\\\b\\\\s+\\\\b")
            // End with a word boundary.
            + "\\b";

        regexPhrase = regexPhrase
            // Move various punctations so they aren't considered part
            // of a word.
            .replace("?\\b", "\\b?")
            .replace(".\\b", "\\b.")
            .replace(",\\b", "\\b,")
            .replace("!\\b", "\\b!")
            .replace(";\\b", "\\b;")
            .replace("-\\b", "\\b-")
            .replace("\\b'", "'\\b")
            // Quote any question marks to avoid problems.
            .replace("?", "\\?");

        // Ignore case and search for phrases that split across lines.
        mPattern = Pattern.compile(regexPhrase,
                                   Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        // Create a regex matcher.
        mPhraseMatcher = mPattern.matcher(input);

        // Initialize the fields.
        mInput = input;
        mMinSplitSize = input.length() / 2;
    }

    /**
     * This constructor is used internally by the compute() method.
     * It initializes all the fields for the "left hand size" of a
     * split.
     */
    private PhraseMatchTask(CharSequence input,
                            String phrase,
                            Pattern pattern,
                            int minSplitSize,
                            int offset) {
        mPattern = pattern;
        mPhraseMatcher = mPattern.matcher(input);
        mInput = input;
        mPhrase = phrase;
        mMinSplitSize = minSplitSize;
        mOffset = offset;
    }
    
    /**
     * Try to find phrase matches sequentially.
     */
    private List<SearchResults.Result> computeSequentially () {
        return StreamSupport
            // Use the MatcherSpliterator to create a new stream of
            // MatchResults.
            .stream(new MatcherSpliterator(mPhraseMatcher),
                    // Create a sequential stream.
                    false)

            // Map each MatchResult into a SearchResults.Result.
            .map(mr -> new SearchResults.Result(mOffset + mr.start()))

            // Collect all the results into a list.
            .collect(toList());
    }

    /**
     * Compute the results of matching the phrase in the input.
     */
    @Override
    public List<SearchResults.Result> compute() {
        // Compute sequentially if the input is too small to split
        // further.
        if (mInput.length() < mMinSplitSize)
            return computeSequentially();
        else {
            // Compute a candidate position for splitting the input.
            int startPos, splitPos = mInput.length() / 2;

            // Get the position to start determining if a phrase spans
            // the split position.
            if ((startPos = computeStartPos(splitPos)) < 0)
                return null;

            // Update splitPos if a phrase spans across the initial
            // splitPos.
            if ((splitPos = tryToUpdateSplitPos(startPos, splitPos)) < 0)
                return null;

            // Create a new PhraseMatchTask that handles the "left
            // hand" portion of the input, while the "this" object
            // handles the "right hand" portion of the input.
            return splitInput(splitPos);
        }
    }

    /**
     * Determine the position to start determining if a phrase spans
     * the split position.  Returns -1 if the phrase is too long for
     * the input.
     */
    private int computeStartPos(int splitPos) {
        // Length of the phrase in non-regex characters.
        int phraseLength = mPhrase.length();
        
        // Subtract the phrase length so we can check to make sure the
        // phrase doesn't span across splitPos.
        int startPos = splitPos - phraseLength;

        // Check if phrase is too long for this input segment.
        if (startPos < 0 || phraseLength > splitPos) 
            return -1;
        else
            return startPos;
    }

    /**
     * Update splitPos if a phrase spans across the initial splitPos.
     */
    private int tryToUpdateSplitPos(int startPos,
                                    int splitPos) {
        // Add length of the phrase in regex characters.
        int endPos = splitPos + mPattern.toString().length();

        // Make sure endPos isn't larger than the input string!
        if (endPos >= mInput.length())
            return -1;

        // Create a substring to check for the case where a phrase
        // spans across the initial splitPos.
        CharSequence substr =
            mInput.subSequence(startPos,
                               endPos);

        // Create a pattern matcher for the substring.
        Matcher phraseMatcher = mPattern.matcher(substr);

        // Check to see if the phrase matches within the substring.
        if (phraseMatcher.find()) 
            // If there's a match update the splitPos to account for
            // the phrase that spans newlines.
            splitPos = startPos 
                + phraseMatcher.start() 
                + phraseMatcher.group().length();

        return splitPos;
    }

    /**
     * Use the fork-join framework to recursively split the input and
     * return a list of SearchResults.Result objects that contain all
     * matching phrases in the input.
     */
    private List<SearchResults.Result> splitInput(int splitPos) {
        // Create and fork a new PhraseMatchTask that concurrently
        // handles the "left hand" portion of the input, while "this"
        // handles the "right hand" portion of the input.
        ForkJoinTask<List<SearchResults.Result>> leftTask =
            new PhraseMatchTask(mInput.subSequence(0, splitPos),
                                mPhrase,
                                mPattern,
                                mMinSplitSize,
                                mOffset).fork();

        // Update the offset.
        mOffset += splitPos;
            
        // Update "this" PhraseMatchTask to handle the "right hand"
        // portion of the input.
        mInput = mInput.subSequence(splitPos, mInput.length());
        mPhraseMatcher = mPattern.matcher(mInput);

        // Recursively call compute() to continue the splitting.
        List<SearchResults.Result> rightResult = compute();

        // Wait and join the results from the left task.
        List<SearchResults.Result> leftResult = leftTask.join();

        // sConcatenate the left result with the right result.
        leftResult.addAll(rightResult);

        // Return the result.
        return leftResult;
    }
}
