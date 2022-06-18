package chapter1;

/**
 * @author lili
 * @date 2022/6/19 1:38
 */
public class ConcreteTank extends AbstractTank {
    protected final float capacity = 10.f;
    protected float volume;

    // ...
    public float getVolume() {
        return volume;
    }

    public float getCapacity() {
        return capacity;
    }

    protected void doTransferWater(float amount)
            throws OverflowException, UnderflowException {
        // ... implementation code ...
    }
}
