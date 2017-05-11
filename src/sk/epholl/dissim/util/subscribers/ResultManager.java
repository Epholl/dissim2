package sk.epholl.dissim.util.subscribers;

import javax.swing.*;
import java.util.*;

/**
 * Created by Tomáš on 05.05.2017.
 */
public class ResultManager {

    private Map<ValueType<?>, List<Subscriber>> valueTypeToSubscriberMapping;

    private LinkedHashMap<ValueType<?>, LinkedList<Object>> emittedValuesBuffer;

    private HashMap<ValueType<?>, Object> lastValues;

    public ResultManager() {
        this.valueTypeToSubscriberMapping = new HashMap<>();
        this.emittedValuesBuffer = new LinkedHashMap<>();
        this.lastValues = new HashMap<>();
    }

    public synchronized <T> void addSubscriber(final ValueType value, final Subscriber<T> subscriber) {
        valueTypeToSubscriberMapping.compute(value, (v, subscriberList) -> {
           if (subscriberList == null) {
               subscriberList = new LinkedList<>();
           }
           subscriberList.add(subscriber);
           return subscriberList;
        });
    }

    public synchronized void removeSubscriber(final Subscriber removed) {
        for (Map.Entry<ValueType<?>, List<Subscriber>> valueTypeSubscribers: valueTypeToSubscriberMapping.entrySet()) {
            List<Subscriber> subscribers = valueTypeSubscribers.getValue();
            if (removed != null) {
                subscribers.remove(removed);
            }
        }
    }

    public synchronized void addValue(final ValueType type, final Object value) {
        type.clazz.cast(value);
        lastValues.put(type, value);
        emittedValuesBuffer.compute(type, (t, valueList) -> {
            if (valueList == null) {
                valueList = new LinkedList<>();
            }
            valueList.add(value);
            return valueList;
        });
    }

    public <T> T getLastValue(final ValueType type) {
        return (T) lastValues.get(type);
    }

    public synchronized void flush() {
        for (Map.Entry<ValueType<?>, LinkedList<Object>> entry: emittedValuesBuffer.entrySet()) {

            final List<Subscriber> entrySubscribers = valueTypeToSubscriberMapping.get(entry.getKey());

            if (entrySubscribers != null) {

                for (Object value: entry.getValue()) {
                    for (Subscriber subscriber: entrySubscribers) {
                        subscriber.onValueEmitted(value);
                    }
                }
            }
        }
        emittedValuesBuffer.clear();
    }

    public void swingFlush() {
        SwingUtilities.invokeLater(() -> flush());
    }

}
