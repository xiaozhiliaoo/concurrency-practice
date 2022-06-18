package chapter3;

/**
 * @author lili
 * @date 2022/6/19 2:06
 */
public class ExceptionEvent extends java.util.EventObject {
    public final Throwable theException;

    public ExceptionEvent(Object src, Throwable ex) {
        super(src);
        theException = ex;
    }
}
