package chapter1;

/**
 * @author lili
 * @date 2022/6/19 1:38
 */
public abstract class AbstractTank implements Tank {
    protected void checkVolumeInvariant() throws java.lang.AssertionError {
        // ... identical to AdaptedTank version ...
    }

    protected abstract void doTransferWater(float amount)
            throws OverflowException, UnderflowException;

    public synchronized void transferWater(float amount)
            throws OverflowException, UnderflowException {
        // identical to AdaptedTank version except for inner call:

        // ...
        try {
            doTransferWater(amount);
        } finally {
        }
        // ...
    }
}
