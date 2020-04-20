package org.example.ex20;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * This class implements the Singleton pattern to handle command-line
 * option processing.
 */
public class Options {
    /** 
     * The singleton @a Options instance. 
     */
    private static Options mUniqueInstance = null;

    /**
     * The path to the image directory.
     */
    private static final String IMAGE_DIRECTORY_PATH =
        "DownloadImages";

    /**
     * Default image names to use for testing.
     */
    private final String[] mDefaultImageNames = new String[] {
        "1.png,"
        + "9.jpg,"
        + "10.png,"
        + "11.jpg,"
        + "16.jpg,"
        + "19.png,"
        + "12.jpg",
        "13.png,"
        + "14.jpg,"
        + "15.jpg,"
        + "17.jpg,"
        + "2.jpg,"
        + "20.jpg,"
        + "3.png",
        "4.png,"
        + "5.jpg,"
        + "21.jpg,"
        + "6.jpg,"
        + "7.png,"
        + "18.jpg,"
        + "8.jpg",
        "1.png,"
        + "9.jpg,"
        + "10.png,"
        + "11.jpg,"
        + "16.jpg,"
        + "19.png,"
        + "12.jpg",
        "13.png,"
        + "14.jpg,"
        + "15.jpg,"
        + "17.jpg,"
        + "2.jpg,"
        + "20.jpg,"
        + "3.png",
        "4.png,"
        + "5.jpg,"
        + "21.jpg,"
        + "6.jpg,"
        + "7.png,"
        + "18.jpg,"
        + "8.jpg"
    };

    /**
     * Prefix for all the URLs.
     */
    private static String sURL_PREFIX =
        "http://www.dre.vanderbilt.edu/~schmidt/images/";

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
     * Return the path for the directory where images are stored.
     */
    public String getDirectoryPath() {
        return new File(IMAGE_DIRECTORY_PATH).getAbsolutePath();
    }

    /**
     * Returns a list of URLs.
     */
    public List<URL> getUrlList() {
        return Stream
            // Convert the array of strings into a stream of strings.
            .of(mDefaultImageNames)

            // Map each string in the list into a list of URLs.
            .flatMap(this::convertStringToUrls)

            // Create and return a list of a list of URLs.
            .collect(toList());
    }

    /**
     * Create a new URL list from a @a stringOfUrls that contains the
     * sURL_PREFIX list of names separated by commas and add them to
     * the URL list that's returned.
     */
    private Stream<URL> convertStringToUrls(String stringOfNames) {
        // Create a Function that returns a new URL object when
        // applied and which converts checked URL exceptions into
        // runtime exceptions.
        Function<String, URL> urlFactory = 
            ExceptionUtils.rethrowFunction(URL::new);

        return Pattern
            // Create a regular expression for the "," separator.
            .compile(",")

            // Use regular expression to split stringOfNames into a
            // Stream<String>.
            .splitAsStream(stringOfNames)

            // Concatenate the url prefix with each name.
            .map(name -> sURL_PREFIX + name)

            // Convert each string in the stream to a URL.
            .map(urlFactory);
    }

    /**
     * Returns whether debugging output is generated.
     */
    public boolean diagnosticsEnabled() {
        return mDiagnosticsEnabled;
    }

    /**
     * Parse command-line arguments and set the appropriate values.
     */
    public boolean parseArgs(String argv[]) {
        if (argv != null) {
            for (int argc = 0; argc < argv.length; argc += 2)
                switch (argv[argc]) {
                case "-d":
                    mDiagnosticsEnabled = argv[argc + 1].equals("true");
                    break;
                default:
                    printUsage();
                    return false;
                }
            return true;
        } else
            return false;
    }

    /**
     * Print out usage and default values.
     */
    private void printUsage() {
        System.out.println("Usage: ");
        System.out.println("-d [true|false]");
    }

    /**
     * Make the constructor private for a singleton.
     */
    private Options() {
        // Create the path to the image directory.
        File imageDirectory =
            new File(getDirectoryPath());

        // Create the image directory.
        imageDirectory.mkdirs();
    }
}
