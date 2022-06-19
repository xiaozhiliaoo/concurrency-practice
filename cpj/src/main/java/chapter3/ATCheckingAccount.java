package chapter3;

import chapter2.InsufficientFunds;

/**
 * @author lili
 * @date 2022/6/19 10:15
 */
public class ATCheckingAccount extends BankAccount {
    protected ATSavingsAccount savings;
    protected long threshold;
    protected TSBoolean transferInProgress = new TSBoolean();

    public ATCheckingAccount(long t) {
        threshold = t;
    }

    // called only upon initialization
    synchronized void initSavings(ATSavingsAccount s) {
        savings = s;
    }

    protected boolean shouldTry() {
        return balance < threshold;
    }

    void tryTransfer() { // called internally or from savings
        if (!transferInProgress.testAndSet()) { // if not busy ...
            try {
                synchronized (this) {
                    if (shouldTry()) balance += savings.transferOut();
                }
            } finally {
                transferInProgress.clear();
            }
        }
    }

    public synchronized void deposit(long amount)
            throws InsufficientFunds {
        if (balance + amount < 0)
            throw new InsufficientFunds();
        else {
            balance += amount;
            tryTransfer();
        }
    }
}