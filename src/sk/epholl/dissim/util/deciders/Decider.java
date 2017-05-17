package sk.epholl.dissim.util.deciders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tomáš on 16.05.2017.
 */
public abstract class Decider<T> {

    public enum Direction {
        Up, Down
    }

    protected List<Condition<T>> conditions;

    protected T defaultValue;

    public Decider(T defaultValue) {
        this.conditions = new ArrayList<>();
        this.defaultValue = defaultValue;
    }

    public List<Condition<T>> getConditions() {
        return Collections.unmodifiableList(conditions);
    }

    public T evaluate() {
        for (Condition<T> condition: conditions) {
            if (condition.evaluate()) {
                return condition.getReturnValue();
            }
        }
        return defaultValue;
    }

    public void addCondition(Condition<T> condition) {
        conditions.add(condition);
    }

    public void removeCondition(Condition<T> condition) {
        conditions.remove(condition);
    }

    public void moveCondition(Condition<T> condition, Direction direction) {
        int index = conditions.indexOf(condition);
        switch (direction) {
            case Up:
                if (index > 0) {
                    Collections.swap(conditions, index, index-1);
                }
                break;
            case Down:
                if (index < conditions.size()-1) {
                    Collections.swap(conditions, index, index+1);
                }
                break;
        }
    }
}
