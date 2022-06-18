package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:51
 */
public class ServerWithOpenCall {
    private double state;
    private final Helper helper = new Helper();

    private synchronized void updateState() {
        state = 2.0f; //  ...; // set to some new value
    }

    public void service() {
        updateState();
        helper.operation();
    }

    public synchronized double getState() {
        return state;
    }
}