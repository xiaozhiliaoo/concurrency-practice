package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:58
 */
public class UpdatableAccountImpl implements UpdatableAccount {
    private long currentBalance;

    public UpdatableAccountImpl(long initialBalance) {
        currentBalance = initialBalance;
    }

    public synchronized long balance() {
        return currentBalance;
    }

    public synchronized void credit(long amount)
            throws InsufficientFunds {
        if (amount >= 0 || currentBalance >= -amount)
            currentBalance += amount;
        else
            throw new InsufficientFunds();
    }

    public synchronized void debit(long amount)
            throws InsufficientFunds {
        credit(-amount);
    }
}