package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:58
 */
public final class ImmutableAccount implements Account {
    private Account delegate;

    public ImmutableAccount(long initialBalance) {
        delegate = new UpdatableAccountImpl(initialBalance);
    }

    ImmutableAccount(Account acct) {
        delegate = acct;
    }

    public long balance() { // forward the immutable method
        return delegate.balance();
    }
}
