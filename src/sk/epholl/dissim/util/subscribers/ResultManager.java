package sk.epholl.dissim.util.subscribers;

import javax.swing.*;
import java.util.*;

/**
 * Created by Tomáš on 05.05.2017.
 */
public class ResultManager {

    private Map<ResultValueType, List<Subscriber>> subscribers;

    private LinkedHashMap<ResultValueType, LinkedList<Object>> values;

    public ResultManager() {
        this.subscribers = new HashMap<>();
        this.values = new LinkedHashMap<>();
    }

    public <T> void addSubscriber(final ResultValueType value, final Subscriber<T> subscriber) {
        subscribers.compute(value, (v, subscriberList) -> {
           if (subscriberList == null) {
               subscriberList = new LinkedList<>();
           }
           subscriberList.add(subscriber);
           return subscriberList;
        });
    }

    public void addValue(final ResultValueType type, final Object value) {
        type.clazz.cast(value);
        values.compute(type, (t, valueList) -> {
            if (valueList == null) {
                valueList = new LinkedList<>();
            }
            valueList.add(value);
            return valueList;
        });
    }

    public void flush() {
        for (Map.Entry<ResultValueType, LinkedList<Object>> entry: values.entrySet()) {

            final List<Subscriber> entrySubscribers = subscribers.get(entry.getKey());

            if (entrySubscribers != null) {

                for (Object value: entry.getValue()) {
                    for (Subscriber subscriber: entrySubscribers) {
                        subscriber.onValueEmitted(value);
                    }
                }
            }
        }
        values.clear();
    }

    public void swingFlush() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                flush();
            }
        });
    }

}
