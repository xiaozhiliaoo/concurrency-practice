package org.example.SearchStreamSpliterator.utils;

/**
 * An options singleton.
 */
public class Options {
    /**
     * The one and only singleton unique instance.
     */
    private static Options sInstance;
    
    /**
     * Return the one and only singleton unique instance.
     */
    public static Options getInstance() {
        if (sInstance == null)
            sInstance = new Options();

        return sInstance;
    }

    /**
     * Stores the designated input separator.
     */
    private String mInputSeparator = "@";

    /**
     * Stores whether the user wants verbose output or not.  Default
     * is no verbose output.
     */
    private boolean mVerbose = false;

    /**
     * Stores the parallel mode ("all");
     */
    private String mParallelMode = "";

    /**
     * Must have a private constructor.
     */
    private Options() {
    }

    /**
     * Parse command-line arguments and set the appropriate values.
     */
    public boolean parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-s")) 
                // Set the file separator character.
                mInputSeparator = args[++i];
            else if (args[i].equals("-v")) 
                mVerbose = true;
            else if (args[i].equals("-p"))
                mParallelMode = args[++i];
            else {
                printUsage();
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the input separator.
     */
    public String getInputSeparator() {
        return mInputSeparator;
    }

    /**
     * Returns true if the user wants "verbose" output, else false.
     */
    public boolean isVerbose() {
        return mVerbose;
    }

    /**
     * Returns the parallel mode.
     */
    public String getParallelMode() {
        return mParallelMode;
    }
    
    /** 
     * Print out usage and default values. 
     */
    private void printUsage() {
        System.out.println("Options usage: \n-s [input-separator] -v -p [parallel-mode]");
    }
}
