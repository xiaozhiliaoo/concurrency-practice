package chapter3;

/**
 * @author lili
 * @date 2022/6/19 2:05
 */
public interface ServerWithException {
    void service() throws ServiceException;
}