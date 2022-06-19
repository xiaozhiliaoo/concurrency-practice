package chapter3;

import chapter2.InsufficientFunds;

/**
 * @author lili
 * @date 2022/6/19 10:21
 */
public class AccountUser {
    TransactionLogger log;                // a made-up class

    // helper method called on any failure
    void rollback(Transaction t, long amount,
                  TransBankAccount src, TransBankAccount dst) {
        log.cancelLogEntry(t, amount, src, dst);
        src.abort(t);
        dst.abort(t);
    }

    public boolean transfer(long amount,
                            TransBankAccount src,
                            TransBankAccount dst)
            throws FailedTransferException, RetryableTransferException {

        if (src == null || dst == null)        // screen arguments
            throw new IllegalArgumentException();
        if (src == dst) return true;           // avoid aliasing

        Transaction t = new Transaction();
        log.logTransfer(t, amount, src, dst);  // record

        if (!src.join(t) || !dst.join(t)) {    // cannot join
            rollback(t, amount, src, dst);
            throw new RetryableTransferException();
        }

        try {
            src.withdraw(t, amount);
            dst.deposit(t, amount);
        } catch (InsufficientFunds ex) {         // semantic failure
            rollback(t, amount, src, dst);
            return false;
        } catch (Failure k) {                    // transaction error
            rollback(t, amount, src, dst);
            throw new RetryableTransferException();
        }

        if (!src.canCommit(t) || !dst.canCommit(t)) { // interference
            rollback(t, amount, src, dst);
            throw new RetryableTransferException();
        }

        try {
            src.commit(t);
            dst.commit(t);
            log.logCompletedTransfer(t, amount, src, dst);
            return true;
        } catch (Failure k) {                    // commitment failure
            rollback(t, amount, src, dst);
            throw new FailedTransferException();
        }

    }
}
