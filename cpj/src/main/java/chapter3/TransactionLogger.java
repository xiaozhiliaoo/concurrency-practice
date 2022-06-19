package chapter3;

/**
 * @author lili
 * @date 2022/6/19 10:20
 */
public class TransactionLogger {
    void cancelLogEntry(Transaction t, long amount,
                        TransBankAccount src, TransBankAccount dst) {
    }

    void logTransfer(Transaction t, long amount,
                     TransBankAccount src, TransBankAccount dst) {
    }

    void logCompletedTransfer(Transaction t, long amount,
                              TransBankAccount src, TransBankAccount dst) {
    }

}
