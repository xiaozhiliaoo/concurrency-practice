package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:41
 */
public class ImmutableAdder {
    private final int offset;

    public ImmutableAdder(int a) {
        offset = a;
    }

    public int addOffset(int b) {
        return offset + b;
    }
}

