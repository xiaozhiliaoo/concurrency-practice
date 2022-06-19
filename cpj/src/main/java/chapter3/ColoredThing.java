package chapter3;

import EDU.oswego.cs.dl.util.concurrent.PropertyChangeMulticaster;
import EDU.oswego.cs.dl.util.concurrent.VetoableChangeMulticaster;

import java.awt.*;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

/**
 * @author lili
 * @date 2022/6/19 10:21
 */
public class ColoredThing {

    protected Color myColor = Color.red; // the sample property
    protected boolean changePending;

    // vetoable listeners:
    protected final VetoableChangeMulticaster vetoers =
            new VetoableChangeMulticaster(this);

    // also some ordinary listeners:
    protected final PropertyChangeMulticaster listeners =
            new PropertyChangeMulticaster(this);

    // registration methods, including:
    void addVetoer(VetoableChangeListener l) {
        vetoers.addVetoableChangeListener(l);
    }

    public synchronized Color getColor() { // property accessor
        return myColor;
    }

    // internal helper methods
    protected synchronized void commitColor(Color newColor) {
        myColor = newColor;
        changePending = false;
    }

    protected synchronized void abortSetColor() {
        changePending = false;
    }

    public void setColor(Color newColor)
            throws PropertyVetoException {
        Color oldColor = null;
        boolean completed = false;

        synchronized (this) {

            if (changePending) { // allow only one transaction at a time
                throw new PropertyVetoException(
                        "Concurrent modification", null);
            } else if (newColor == null) {   // Argument screening
                throw new PropertyVetoException(
                        "Cannot change color to Null", null);
            } else {
                changePending = true;
                oldColor = myColor;
            }
        }

        try {
            vetoers.fireVetoableChange("color", oldColor, newColor);
            // fall through if no exception:
            commitColor(newColor);
            completed = true;
            // notify other listeners that change is committed
            listeners.firePropertyChange("color", oldColor, newColor);
        } catch (PropertyVetoException ex) { // abort on veto
            abortSetColor();
            completed = true;
            throw ex;
        } finally {                    // trap any unchecked exception
            if (!completed) abortSetColor();
        }
    }
}