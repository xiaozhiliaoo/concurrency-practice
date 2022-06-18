package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:57
 */
public interface UpdatableAccount extends Account {
    void credit(long amount) throws InsufficientFunds;

    void debit(long amount) throws InsufficientFunds;
}
