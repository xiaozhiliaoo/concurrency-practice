package chapter3;

import chapter2.InsufficientFunds;

/**
 * @author lili
 * @date 2022/6/19 10:16
 */
public class ATSavingsAccount extends BankAccount {

    protected ATCheckingAccount checking;
    protected long maxTransfer;

    public ATSavingsAccount(long max) {
        maxTransfer = max;
    }

    // called only upon initialization
    synchronized void initChecking(ATCheckingAccount c) {
        checking = c;
    }

    synchronized long transferOut() { // called only from checking
        long amount = balance;
        if (amount > maxTransfer)
            amount = maxTransfer;
        if (amount >= 0)
            balance -= amount;
        return amount;
    }

    public synchronized void deposit(long amount)
            throws InsufficientFunds {
        if (balance + amount < 0)
            throw new InsufficientFunds();
        else {
            balance += amount;
            checking.tryTransfer();
        }
    }

}
