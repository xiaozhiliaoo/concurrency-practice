package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:42
 */
public class Relay {
    protected final Server server;

    Relay(Server s) {
        server = s;
    }

    void doIt() {
        server.doIt();
    }
}