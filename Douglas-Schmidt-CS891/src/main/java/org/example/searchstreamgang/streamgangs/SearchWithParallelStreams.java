package org.example.searchstreamgang.streamgangs;

import java.util.List;
import java.util.function.Predicate;

import org.example.searchstreamgang.utils.SearchResults;

import static java.util.stream.Collectors.toList;
import static org.example.searchstreamgang.utils.StreamsUtils.not;

/**
 * Customizes the SearchStreamGang framework to use Java parallel
 * streams to perform a parallel search of each input data string and
 * each phrase (from an array of phrases) within each input data
 * string.
 */
public class SearchWithParallelStreams extends SearchStreamGang {
    /**
     * Constructor initializes the super class.
     */
    public SearchWithParallelStreams(List<String> phrasesToFind,
                                     List<List<CharSequence>> stringsToSearch) {
        // Pass input to superclass constructor.
        super(phrasesToFind,
              stringsToSearch);
    }

    /**
     * Perform the processing, which uses a Java 8 Stream to search
     * for phrases in the input data in parallel.
     */
    @Override
    protected List<List<SearchResults>> processStream() {
        // Get the list of input strings.
        List<CharSequence> inputList = getInput();

    	// Process the input strings via a parallel stream.
        return inputList
            // Concurrently process each string in the input list.
            .parallelStream()

            // Map each input string to list of SearchResults
            // containing the phrases found in the input.
            .map(this::processInput)

            // Terminate stream and return a list of lists of
            // SearchResults.
            .collect(toList());
    }
    
    /**
     * Concurrently search @a inputSeq for all occurrences of the
     * phrases to find.
     */
    private List<SearchResults> processInput(CharSequence inputSeq) {
        // Get the section title.
        String title = getTitle(inputSeq);

        // Skip over the title.
        CharSequence input = inputSeq.subSequence(title.length(),
                                                  inputSeq.length());

        // Iterate through each phrase we're searching for and try to
        // find it in the inputData.
        List<SearchResults> results = mPhrasesToFind
            // Convert the list of phrases into a parallel stream.
            .parallelStream()
            
            // Find all indices where phrase matches the input data.
            .map(phrase -> searchForPhrase(phrase,
                                           input,
                                           title,
                                           false))
            
            // Only keep a result that has at least one match.
            .filter(not(SearchResults::isEmpty))
            // Filtering can also be done as
            // .filter(result -> result.size() > 0)
            
            // Terminate stream and return a list of SearchResults.
            .collect(toList());
            
        // Return the results.
        return results;
    }
}

