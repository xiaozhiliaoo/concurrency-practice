package chapter1;

/**
 * @author lili
 * @date 2022/6/19 1:40
 */
public class TankWithMethodAdapter {
    // ...
    protected void checkVolumeInvariant() throws java.lang.AssertionError {
        // ... identical to AdaptedTank version ...
    }

    protected void runWithinBeforeAfterChecks(TankOp cmd)
            throws OverflowException, UnderflowException {
        // identical to AdaptedTank.transferWater
        //   except for inner call:

        // ...
        try {
            cmd.op();
        } finally {
        }

        // ...
    }

    protected void doTransferWater(float amount)
            throws OverflowException, UnderflowException {
        // ... implementation code ...
    }

    public synchronized void transferWater(final float amount)
            throws OverflowException, UnderflowException {

        runWithinBeforeAfterChecks(new TankOp() {
            public void op()
                    throws OverflowException, UnderflowException {
                doTransferWater(amount);
            }
        });
    }
}
