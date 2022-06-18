package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:52
 */
public class PassThroughShape {

    protected final AdjustableLoc loc = new AdjustableLoc(0, 0);
    protected final AdjustableDim dim = new AdjustableDim(0, 0);

    public double x() {
        return loc.x();
    }

    public double y() {
        return loc.y();
    }

    public double width() {
        return dim.width();
    }

    public double height() {
        return dim.height();
    }

    public void adjustLocation() {
        loc.adjust();
    }

    public void adjustDimensions() {
        dim.adjust();
    }
}
