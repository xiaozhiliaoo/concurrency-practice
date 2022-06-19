package chapter3;

import chapter2.InsufficientFunds;

/**
 * @author lili
 * @date 2022/6/19 10:18
 */
public interface TransBankAccount extends Transactor {

    public long balance(Transaction t) throws Failure;

    public void deposit(Transaction t, long amount)
            throws InsufficientFunds, Failure;

    public void withdraw(Transaction t, long amount)
            throws InsufficientFunds, Failure;

}
