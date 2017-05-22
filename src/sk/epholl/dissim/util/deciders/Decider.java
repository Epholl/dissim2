package sk.epholl.dissim.util.deciders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tomáš on 16.05.2017.
 */
public abstract class Decider<T> {

    protected List<Condition<T>> conditions;

    protected T defaultValue;

    public Decider(T defaultValue) {
        this.conditions = new ArrayList<>();
        this.defaultValue = defaultValue;
    }


    public T evaluate() {
        for (Condition<T> condition: conditions) {
            if (condition.evaluate()) {
                return condition.getReturnValue();
            }
        }
        return defaultValue;
    }

    public void clear() {
        conditions.clear();
    }

    public void addCondition(Condition<T> condition) {
        conditions.add(condition);
    }
}
