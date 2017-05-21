package sk.epholl.dissim.sem3.ui;

import sk.epholl.dissim.util.subscribers.ResultManager;
import sk.epholl.dissim.util.subscribers.Subscriber;
import sk.epholl.dissim.util.subscribers.ValueType;

import javax.swing.*;

/**
 * Created by Tomáš on 18.05.2017.
 */
public class JLabelResultController {
    private String name;
    private JLabel label;

    public JLabelResultController(String name, JLabel label, ValueType<?> valueType, ResultManager rm) {
        this.name = name;
        this.label = label;
        label.setText(name);

        rm.addSubscriber(valueType, this::valueEmitted);
    }

    public void valueEmitted(Object value) {
        label.setText(name + " " + value.toString());
    }
}
