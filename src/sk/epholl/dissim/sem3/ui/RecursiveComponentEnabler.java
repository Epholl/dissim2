package sk.epholl.dissim.sem3.ui;

/**
 * Stack overflow:
 * http://stackoverflow.com/questions/305527/how-to-disable-a-container-and-its-children-in-swing
 */
import java.awt.Component;
import java.awt.Container;
import java.util.Map;
import java.util.WeakHashMap;

public class RecursiveComponentEnabler {

    public static void setEnabled(Component component, boolean enabled) {
        final int val = enabled? +1 : -1;
        setEnabledRecursive(component, enabled);
    }

    // val = 1 for enabling, val = -1 for disabling
    private static void setEnabledRecursive(Component component, boolean enabled) {
        if (component != null) {

            component.setEnabled(enabled);

            if (component instanceof Container) {
                Container componentAsContainer = (Container) component;
                for (Component c : componentAsContainer.getComponents()) {
                    setEnabledRecursive(c, enabled);
                }
            }
        }
    }

}