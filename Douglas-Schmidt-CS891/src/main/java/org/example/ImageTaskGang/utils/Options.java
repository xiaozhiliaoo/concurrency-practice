package org.example.ImageTaskGang.utils;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * This class implements the Singleton pattern to handle
 * command-line option processing.
 */
public class Options {
    /** The singleton @a Options instance. */
    private static Options mUniqueInstance = null;

    /**
     * An enumeration of each different input source.
     */
    public enum InputSource {
    	DEFAULT,        // The default remote input source.
        DEFAULT_LOCAL,  // The default local input source.
        USER,           // Input from a user-defined source.
        FILE,           // Input from a delimited file.
        NETWORK,        // Input from a network call
        ERROR           // Returned if source is unrecognized.
    }

    /**
     * Keep track of the source of the inputs.
     */
    InputSource mInputSource = InputSource.DEFAULT_LOCAL;

    /**
     * Default URLs used for testing.
     */
    private final String[] mDefaultUrls = new String[] {        
          "http://www.dre.vanderbilt.edu/~schmidt/gifs/ka.png,"
        + "http://www.dre.vanderbilt.edu/~schmidt/gifs/uci.png,"
        + "http://www.dre.vanderbilt.edu/~schmidt/gifs/dougs_small.jpg,"
        + "http://www.dre.vanderbilt.edu/~schmidt/gifs/kitten.png,"
        + "http://www.dre.vanderbilt.edu/~schmidt/gifs/schmidt_coursera.jpg,"
        + "http://www.dre.vanderbilt.edu/~schmidt/gifs/dark_rider.jpg,"
        + "http://www.dre.vanderbilt.edu/~schmidt/gifs/doug.jpg",
          "http://www.dre.vanderbilt.edu/~schmidt/gifs/lil_doug.jpg,"
        + "http://www.dre.vanderbilt.edu/~schmidt/gifs/ironbound.jpg,"
        + "http://www.dre.vanderbilt.edu/~schmidt/gifs/wm.jpg,"
        + "http://www.dre.vanderbilt.edu/~schmidt/gifs/robot.png,"
        + "http://www.dre.vanderbilt.edu/~schmidt/gifs/ace_copy.jpg,"
        + "http://www.dre.vanderbilt.edu/~schmidt/gifs/tao_copy.jpg,"
        + "http://www.dre.vanderbilt.edu/~schmidt/gifs/doug_circle.png"
    };

    /**
     * Pathname for the file containing URLs to download.
     */
    private final String mPathname = "defaultUrls.txt";

    /**
     * Controls whether debugging output will be generated (defaults
     * to false).
     */
    private boolean mDiagnosticsEnabled = false;

    /**
     * Method to return the one and only singleton uniqueInstance.
     */
    public static Options instance() {
        if (mUniqueInstance == null)
            mUniqueInstance = new Options();

        return mUniqueInstance;
    }

    /**
     * Return pathname for the file containing the URLs to download.
     */
    public String getURLFilePathname() {
        return mPathname;
    }

    /**
     * Return the path for the directory where images are stored.
     */
    public String getDirectoryPath() {
        return new File("DownloadImages").getAbsolutePath();
    }

    /**
     * Takes a string input and returns the corresponding InputSource.
     */
    private InputSource getInputSource(String inputSource) {
        if (inputSource.equalsIgnoreCase("DEFAULT"))
            return InputSource.DEFAULT;
        else if (inputSource.equalsIgnoreCase("DEFAULT_LOCAL"))
            return InputSource.DEFAULT_LOCAL;
        else if (inputSource.equalsIgnoreCase("USER"))
            return InputSource.USER;
        else if (inputSource.equalsIgnoreCase("FILE"))
            return InputSource.FILE;
        else if (inputSource.equalsIgnoreCase("NETWORK"))
            return InputSource.NETWORK;
        else
            return InputSource.ERROR;
    }

    /**
     * Return an Iterator over one or more input URL Lists.
     */
    public Iterator<List<URL>> getUrlIterator() {
        List<List<URL>> urlLists =
            getUrlLists();
    	return urlLists != null && urlLists.size() > 0
            ? urlLists.iterator()
            : null;
    }

    /**
     * Gets the list of lists of URLs from which we want to download
     * images.
     */
    public List<List<URL>> getUrlLists() {
    	try {
            switch (mInputSource) {
            // If the user selects the defaults source, return the
            // default list of remote URL lists.
            case DEFAULT:
                return getDefaultUrlList(false);

            // If the user selects the default_local source, return the
            // default list of local URL lists.
            case DEFAULT_LOCAL:
                return getDefaultUrlList(true);

            // Take input from the Android UI.
            case USER:
            	/*
                for (int i = 0; i < numChildViews; ++i) {

                    // Convert the input string into a list of URLs
                    // and add it to the main list.
                    variableNumberOfInputURLs.add
                        (convertStringToUrls(child.getText().toString()));
                }
                */

                break;

            default:
                System.out.println("Invalid Source");
                return null;
            }
    	} catch (MalformedURLException e) {
            System.out.println("Invalid URL");
            return null;
    	}
    	return null;
    }

    /**
     * Returns the appropriate list of URLs, i.e., either pointing to
     * the local device or to a remote server.
     */
    protected List<List<URL>> getDefaultUrlList(boolean local)
        throws MalformedURLException {
        return local
               ? getDefaultResourceUrlList()
               : getDefaultUrlList();
    }

    /**
     * Returns a List of default URL Lists that is usable in either
     * platform.
     */
    protected List<List<URL>> getDefaultUrlList()
            throws MalformedURLException {
        /*
          List<List<URL>> variableNumberOfInputURLs = new ArrayList<>();

          // Convert all the suggestion strings into URLs.
          for (String suggestedUrls : mDefaultUrls)
            variableNumberOfInputURLs.add
                (convertStringToUrls(suggestedUrls));
          return variableNumberOfInputURLs;
        */
        // Convert the array of strings into a list of strings.
        // Convert the list into a stream.
        return Arrays
            .stream(mDefaultUrls)

            // Map each string in the list into a list of URLs.
            .map(this::convertStringToUrls)

            // Create and return a list of a list of URLs.
            .collect(toList());
    }

    /**
     * Returns a List of default URL Lists that is usable in either
     * platform.
     */
    protected List<List<URL>> getDefaultResourceUrlList()
            throws MalformedURLException {
        // Create a two-dimensional array of URLs to images on the local device.
        URL[][] urlsArray = new URL[][] {
            {
            	ClassLoader.getSystemResource("schmidt_coursera.jpg"),
            	ClassLoader.getSystemResource("dark_rider.jpg"),
            	ClassLoader.getSystemResource("ka.png"),
            	ClassLoader.getSystemResource("uci.png"),
            	ClassLoader.getSystemResource("dougs_small.jpg"),
            	ClassLoader.getSystemResource("kitten.png"),
            	ClassLoader.getSystemResource("doug.jpg")
            },
            {    
            	ClassLoader.getSystemResource("ace_copy.jpg"),
            	ClassLoader.getSystemResource("tao_copy.jpg"),
            	ClassLoader.getSystemResource("lil_doug.jpg"),
            	ClassLoader.getSystemResource("ironbound.jpg"),
            	ClassLoader.getSystemResource("wm.jpg"),
            	ClassLoader.getSystemResource("robot.png"),
            	ClassLoader.getSystemResource("doug_circle.png")
            }
        };

        // Return a list of lists of URLs.
        return Arrays.stream(urlsArray)
                     .map(Arrays::asList)
                     .collect(toList());
        /*
        List<List<URL>> variableNumberOfInputURLs = new ArrayList<>();

        for (URL[] urls : urlsArray) {
            // Create a new List of URLs containing the next URLs from
            // the array.
            variableNumberOfInputURLs.add(Arrays.asList(urls));
        }

        return variableNumberOfInputURLs;
        */
    }

    /**
     * Create a new URL list from a @a stringOfUrls that contains a
     * list of URLs separated by commas and add them to the URL list
     * that's returned.
     */
    private List<URL> convertStringToUrls(String stringOfUrls) {
        // Create a Function that returns a new URL object when
        // applied.
        Function<String, URL> urlFactory = ExceptionUtils.rethrowFunction(URL::new);

        return
            // Create a regular expression for the "," separator.
            Pattern.compile(",")

            // Use the regular expression to split the strongOfUrls into a Stream<String>.
            .splitAsStream(stringOfUrls)

            // Convert each String in the Stream to a URL.
            .map(urlFactory)

            // Create a list of URLs.
            .collect(toList());
    }

    /**
     * Returns whether debugging output is generated.
     */
    public boolean diagnosticsEnabled() {
        return mDiagnosticsEnabled;
    }

    /**
     * Print out all the timing results for all the test runs in order
     * from fastest to slowest.
     */
    public static void printTimingResults(Map<String, List<Long>> resultsMap) {
        // Determine how many runs of the tests took place.
        int numberOfRuns =
            resultsMap.entrySet().iterator().next().getValue().size();

        // Iterate through the results of each of the test runs.
        for (int i = 0;
             i < numberOfRuns;
             i++) {
            final int runNumber = i;
            System.out.println("\nPrinting "
                               + resultsMap.entrySet().size()
                               + " results for input file "
                               + (runNumber + 1)
                               + " from fastest to slowest");

            // Print out the contents of the resultsMap in sorted
            // order.
            resultsMap
                // Get the entrySet for the resultsMap.
                .entrySet()

                // Convert the entrySet into a stream.
                .stream()

                // Create a SimpleImmutableEntry containing the timing
                // results (value) followed by the test name (key).
                .map(entry
                     -> new AbstractMap.SimpleImmutableEntry<>
                        (entry.getValue().get(runNumber),
                         entry.getKey()))

                // Sort the stream by the timing results (key).
                .sorted(Map.Entry.comparingByKey())

                // Print all the entries in the sorted stream.
                .forEach(entry
                         -> System.out.println(""
                                               + entry.getValue()
                                               + " executed in "
                                               + entry.getKey()
                                               + " msecs"));
        }
    }

    /**
     * Parse command-line arguments and set the appropriate values.
     */
    public void parseArgs(String[] argv) {
        if (argv != null) {
            for (int argc = 0; argc < argv.length; argc += 2)
                if (argv[argc].equals("-d"))
                    mDiagnosticsEnabled = argv[argc + 1].equals("true");
                else if (argv[argc].equals("-s")) {
                    mInputSource = getInputSource(argv[argc + 1]);
                } else {
                    printUsage();
                    return;
                }
        }
    }

    /**
     * Print out usage and default values.
     */
    public void printUsage() {
        System.out.println("Usage: ");
        System.out.println("-d [true|false]");
        System.out.println("-s [DEFAULT|DEFAULT_LOCAL|USER|FILE]");
    }

    /**
     * Make the constructor private for a singleton.
     */
    private Options() {
    }
}
