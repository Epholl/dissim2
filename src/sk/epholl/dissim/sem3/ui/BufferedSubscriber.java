package sk.epholl.dissim.sem3.ui;

import sk.epholl.dissim.util.subscribers.Subscriber;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tomáš on 08.05.2017.
 */
public abstract class BufferedSubscriber <T> implements Subscriber<T> {

    private List<T> emittedValues;

    private Timer timer;

    public BufferedSubscriber() {
        emittedValues = new LinkedList<>();
        timer = new Timer(200, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<T> values = Collections.unmodifiableList(emittedValues);
                onValuesEmitted(values);
                emittedValues.clear();
            }
        });
    }

    @Override
    public void onValueEmitted(T value) {
        emittedValues.add(value);
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    public abstract void onValuesEmitted(List<T> emittedValues);
}
