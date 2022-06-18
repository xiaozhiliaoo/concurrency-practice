package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:58
 */
public class AccountRecorder { // A logging facility
    public void recordBalance(Account a) {
        System.out.println(a.balance()); // or record in file
    }
}
