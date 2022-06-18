package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:58
 */
public class AccountHolder {
    private UpdatableAccount acct = new UpdatableAccountImpl(0);
    private AccountRecorder recorder;

    public AccountHolder(AccountRecorder r) {
        recorder = r;
    }

    public synchronized void acceptMoney(long amount) {
        try {
            acct.credit(amount);
            recorder.recordBalance(new ImmutableAccount(acct));//(*)
        } catch (InsufficientFunds ex) {
            System.out.println("Cannot accept negative amount.");
        }
    }
}
