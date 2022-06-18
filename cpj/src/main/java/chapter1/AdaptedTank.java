package chapter1;

/**
 * @author lili
 * @date 2022/6/19 1:35
 */
public class AdaptedTank implements Tank {
    protected final Tank delegate;

    public AdaptedTank(Tank t) {
        delegate = t;
    }

    public float getCapacity() {
        return delegate.getCapacity();
    }

    public float getVolume() {
        return delegate.getVolume();
    }

    protected void checkVolumeInvariant() throws AssertionError {
        float v = getVolume();
        float c = getCapacity();
        if (!(v >= 0.0 && v <= c))
            throw new AssertionError();
    }

    public synchronized void transferWater(float amount)
            throws OverflowException, UnderflowException {


        checkVolumeInvariant();  // before-check

        try {
            delegate.transferWater(amount);
        }

        // postpone rethrows until after-check
        catch (OverflowException ex) {
            throw ex;
        } catch (UnderflowException ex) {
            throw ex;
        } finally {
            checkVolumeInvariant(); // after-check
        }
    }

}