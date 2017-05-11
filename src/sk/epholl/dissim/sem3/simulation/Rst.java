package sk.epholl.dissim.sem3.simulation;

import sk.epholl.dissim.util.subscribers.ValueType;

/**
 * Created by Tomáš on 08.05.2017.
 */
public class Rst {

    public static final ValueType<Double> SIMULATION_TIME = new ValueType<>(Double.class);
    public static final ValueType<Integer> REPLICATION_COUNT = new ValueType<>(Integer.class);

    public static final ValueType<String> CONSOLE_LOG = new ValueType<>(String.class);
}
