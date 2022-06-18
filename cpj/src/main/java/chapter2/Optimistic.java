package chapter2;

/**
 * @author lili
 * @date 2022/6/19 2:00
 */
public class Optimistic {                       // Generic code sketch

    private State state; // reference to representation object

    private synchronized State getState() {
        return state;
    }

    private synchronized boolean commit(State assumed,
                                        State next) {
        if (state == assumed) {
            state = next;
            return true;
        } else
            return false;
    }
}
