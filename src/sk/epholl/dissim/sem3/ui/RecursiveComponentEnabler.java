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

    private static final Map<Component, Integer> componentAvailability = new WeakHashMap<Component, Integer>();

    public static void setEnabled(Component component, boolean enabled) {
        final int val = enabled? +1 : -1;
        setEnabledRecursive(component, val);
    }

    // val = 1 for enabling, val = -1 for disabling
    private static void setEnabledRecursive(Component component, int val) {
        if (component != null) {
            final Integer oldValObj = componentAvailability.get(component);
            final int oldVal = (oldValObj == null)
                    ? 0
                    : oldValObj;
            final int newVal = oldVal + val;
            componentAvailability.put(component, newVal);

            if (newVal >= 0) {
                component.setEnabled(true);
            } else if (newVal < 0) {
                component.setEnabled(false);
            }
            if (component instanceof Container) {
                Container componentAsContainer = (Container) component;
                for (Component c : componentAsContainer.getComponents()) {
                    setEnabledRecursive(c,val);
                }
            }
        }
    }

}