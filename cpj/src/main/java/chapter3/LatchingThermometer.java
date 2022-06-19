package chapter3;

/**
 * @author lili
 * @date 2022/6/19 10:13
 */
public class LatchingThermometer {                      // Seldom useful
    private volatile boolean ready; // latching
    private volatile float temperature;

    public double getReading() {
        while (!ready)
            Thread.yield();
        return temperature;
    }

    void sense(float t) { // called from sensor
        temperature = t;
        ready = true;
    }
}
