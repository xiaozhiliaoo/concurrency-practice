package chapter3;

/**
 * @author lili
 * @date 2022/6/19 10:18
 */
public interface Transactor {

    // Enter a new transaction and return true, if can do so
    public boolean join(Transaction t);

    // Return true if this transaction can be committed
    public boolean canCommit(Transaction t);

    // Update state to reflect current transaction
    public void commit(Transaction t) throws Failure;

    // Roll back state (No exception; ignore if inapplicable)
    public void abort(Transaction t);

}
