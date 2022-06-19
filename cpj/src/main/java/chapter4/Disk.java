package chapter4;

import chapter3.Failure;

/**
 * @author lili
 * @date 2022/6/19 10:47
 */
public interface Disk {
    void read(int cylinderNumber, byte[] buffer) throws Failure;

    void write(int cylinderNumber, byte[] buffer) throws Failure;
}
