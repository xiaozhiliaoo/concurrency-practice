package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:51
 */
public class ServerWithStateUpdate {
    private double state;
    private final Helper helper = new Helper();

    public synchronized void service() {
        state = 2.0f; // ...; // set to some new value
        helper.operation();
    }

    public synchronized double getState() {
        return state;
    }
}
