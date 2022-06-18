package chapter3;

/**
 * @author lili
 * @date 2022/6/19 2:06
 */
public class HandledService implements ServerWithException {
    final ServerWithException server = new ServerImpl();
    final ServiceExceptionHandler handler = new HandlerImpl();

    public void service() { // no throw clause
        try {
            server.service();
        } catch (ServiceException e) {
            handler.handle(e);
        }
    }
}
