package chapter3;

/**
 * @author lili
 * @date 2022/6/19 10:19
 */
public class ProxyAccount /* implements TransBankAccount */ {
    private TransBankAccount delegate;

    public boolean join(Transaction t) {
        return delegate.join(t);
    }


    public long balance(Transaction t) throws Failure {
        return delegate.balance(t);
    }

    // and so on...

}
