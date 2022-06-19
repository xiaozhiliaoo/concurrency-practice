package chapter4;

/**
 * @author lili
 * @date 2022/6/19 10:35
 */
public class HorizontallyJoinedPair extends JoinedPair {

    public HorizontallyJoinedPair(Box l, Box r) {
        super(l, r);
    }

    public synchronized Box duplicate() {
        HorizontallyJoinedPair p =
                new HorizontallyJoinedPair(fst.duplicate(),
                        snd.duplicate());
        p.setColor(getColor());
        return p;
    }


    // ... other implementations of abstract Box methods
}
