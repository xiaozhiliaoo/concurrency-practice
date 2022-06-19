package chapter4;

import java.io.IOException;

/**
 * @author lili
 * @date 2022/6/19 10:44
 */
public interface FileReaderClient {
    void readCompleted(String filename, byte[] data);

    void readFailed(String filename, IOException ex);
}