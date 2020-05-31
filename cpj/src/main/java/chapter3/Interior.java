package chapter3;

/**
 * @author lili
 * @date 2020/5/30 22:07
 * @description
 * @notes
 */
public class Interior extends JTree {
    private final JTree[] quads;

    Interior(JTree q1, JTree q2, JTree q3, JTree q4) {
        quads = new JTree[]{q1, q2, q3, q4};
    }

    public void run() {
        coInvoke(quads);
        double md = 0.0;
        for (int i = 0; i < quads.length; ++i) {
            md = Math.max(md, quads[i].maxDiff);
            quads[i].reset();
        }
        maxDiff = md;
    }
}
