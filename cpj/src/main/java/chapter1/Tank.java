package chapter1;

public interface Tank {
    float getCapacity();

    float getVolume();

    void transferWater(float amount)
            throws OverflowException, UnderflowException;
}
