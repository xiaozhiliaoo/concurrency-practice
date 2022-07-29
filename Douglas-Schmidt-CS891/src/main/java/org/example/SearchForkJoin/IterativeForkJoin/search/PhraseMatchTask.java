package org.example.SearchForkJoin.IterativeForkJoin.search;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used in conjunction with the Java fork-join pool to
 * create a list of {@link SearchResults.Result} objects that match
 * the number of times a phrase appears in an input string.  The
 * comparison is case-insensitive.
 */
public class PhraseMatchTask
       extends RecursiveTask<SearchResults> {
    /**
     * The document title
     */
    private String mTitle;

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
     * True if we compute the match in parallel, else false.
     */
    private final boolean mParallelSearch;

    /**
     * Constructor initializes the fields.
     */
    PhraseMatchTask(String title,
                    CharSequence input,
                    String phrase,
                    boolean parallel) {
        // Store the title.
        mTitle = title;

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
            // Move various punctuations so they aren't considered part
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
        mParallelSearch = parallel;
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
                            int offset,
                            boolean parallel) {
        mPattern = pattern;
        mPhraseMatcher = mPattern.matcher(input);
        mInput = input;
        mPhrase = phrase;
        mMinSplitSize = minSplitSize;
        mOffset = offset;
        mParallelSearch = parallel;
    }
    
    /**
     * Compute the results of matching the phrase in the input.
     */
    @Override
    public SearchResults compute() {
        if (mParallelSearch && mInput.length() >= mMinSplitSize)
            return computeParallel();
        else
            return computeSequential();
    }

    /**
     * Try to find phrase matches sequentially.
     */
    private SearchResults computeSequential() {
        // Create the list of results.
        List<SearchResults.Result> list =
            new ArrayList<>();

        // Try to find a phrase match in the input, ignoring case.  If
        // there's no match then we're done with the iteration.
        while (mPhraseMatcher.find())
            // Create/accept a new Result object that stores the index
            // of where the phrase occurs in the original string
            // (which is why we add mOffset).
            list.add(new SearchResults.Result(mOffset + mPhraseMatcher.start()));

        // Return the list.
        return new SearchResults(mPhrase,
                                 mTitle,
                                 list);
    }

    /**
     * Try to find phrase matches in parallel.
     */
    private SearchResults computeParallel() {
        // Compute a candidate position for splitting the input.
        int startPos, splitPos = mInput.length() / 2;

        // Get the position to start determining if a phrase spans
        // the split position.
        if ((startPos = computeStartPos(splitPos)) < 0)
            return new SearchResults("", "", null);

        // Update splitPos if a phrase spans across the initial
        // splitPos.
        if ((splitPos = tryToUpdateSplitPos(startPos, splitPos)) < 0)
            return new SearchResults("", "", null);

        // Create a new PhraseMatchTask that handles the "left
        // hand" portion of the input, while the "this" object
        // handles the "right hand" portion of the input.
        return splitInput(splitPos);
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
    private SearchResults splitInput(int splitPos) {
        // Create and fork a new PhraseMatchTask that concurrently
        // handles the "left hand" portion of the input, while "this"
        // handles the "right hand" portion of the input.
        ForkJoinTask<SearchResults> leftTask =
            new PhraseMatchTask(mInput.subSequence(0, splitPos),
                                mPhrase,
                                mPattern,
                                mMinSplitSize,
                                mOffset,
                                mParallelSearch).fork();

        // Update the offset.
        mOffset += splitPos;
            
        // Update "this" PhraseMatchTask to handle the "right hand"
        // portion of the input.
        mInput = mInput.subSequence(splitPos, mInput.length());
        mPhraseMatcher = mPattern.matcher(mInput);

        // Recursively call compute() to continue the splitting.
        SearchResults rightResult = compute();

        // Wait and join the results from the left task.
        SearchResults leftResult = leftTask.join();

        if (leftResult.isEmpty()) {
            return rightResult;
        } else {
            // Concatenate the left result with the right result.
            leftResult.addAll(rightResult);

            // Return the result.
            return leftResult;
        }
    }
}
