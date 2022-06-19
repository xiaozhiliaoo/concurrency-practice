package chapter4;

import java.io.IOException;

/**
 * @author lili
 * @date 2022/6/19 10:44
 */
public class FileReaderApp implements FileReaderClient { // Fragments
    protected FileReader reader = new AFileReader();

    public void readCompleted(String filename, byte[] data) {
        // ... use data ...
    }

    public void readFailed(String filename, IOException ex) {
        // ... deal with failure ...
    }

    public void actionRequiringFile() {
        reader.read("AppFile", this);
    }

    public void actionNotRequiringFile() {
    }
}
