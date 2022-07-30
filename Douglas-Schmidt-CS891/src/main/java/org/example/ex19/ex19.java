package org.example.ex19;

import org.example.ex19.utils.Options;

/**
 * This example shows how to count the number of images in a
 * recursively-defined folder structure using a range of features from
 * the Java completable futures framework.
 */
public class ex19 {
    /**
     * This static main() entry point runs the example.
     */
    public static void main(String[] args) {
        // Initializes the Options singleton.
        Options.instance().parseArgs(args);

        // Create an object that count the images.
        new ImageCounter();
    }
}
