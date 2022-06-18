package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:59
 */
public class EvilAccountRecorder extends AccountRecorder {
    private long embezzlement;

    // ...
    public void recordBalance(Account a) {
        super.recordBalance(a);

        if (a instanceof UpdatableAccount) {
            UpdatableAccount u = (UpdatableAccount) a;
            try {
                u.debit(10);
                embezzlement += 10;
            } catch (InsufficientFunds quietlyignore) {
            }
        }
    }
}
