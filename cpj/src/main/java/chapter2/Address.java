package chapter2;

import java.io.OutputStream;

/**
 * @author lili
 * @date 2022/6/19 1:49
 */
public class Address {                          // Fragments
    protected String street;
    protected String city;

    public String getStreet() {
        return street;
    }

    public void setStreet(String s) {
        street = s;
    }

    // ...
    public void printLabel(OutputStream s) {
    }
}
