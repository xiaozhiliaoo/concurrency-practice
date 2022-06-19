package chapter3;

import chapter2.InsufficientFunds;

/**
 * @author lili
 * @date 2022/6/19 10:19
 */
public class SimpleTransBankAccount implements TransBankAccount {

    protected long balance = 0;
    protected long workingBalance = 0; // single shadow copy
    protected Transaction currentTx = null; // single transaction

    public synchronized long balance(Transaction t) throws Failure {
        if (t != currentTx) throw new Failure();
        return workingBalance;
    }

    public synchronized void deposit(Transaction t, long amount)
            throws InsufficientFunds, Failure {
        if (t != currentTx) throw new Failure();
        if (workingBalance < -amount)
            throw new InsufficientFunds();
        workingBalance += amount;
    }

    public synchronized void withdraw(Transaction t, long amount)
            throws InsufficientFunds, Failure {
        deposit(t, -amount);
    }

    public synchronized boolean join(Transaction t) {
        if (currentTx != null) return false;
        currentTx = t;
        workingBalance = balance;
        return true;
    }

    public synchronized boolean canCommit(Transaction t) {
        return (t == currentTx);
    }

    public synchronized void abort(Transaction t) {
        if (t == currentTx)
            currentTx = null;
    }

    public synchronized void commit(Transaction t) throws Failure {
        if (t != currentTx) throw new Failure();
        balance = workingBalance;
        currentTx = null;
    }

}
