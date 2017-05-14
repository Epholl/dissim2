package sk.epholl.dissim.util;

import OSPABA.Simulation;

import java.util.HashMap;

/**
 * Created by Tomáš on 05.05.2017.
 */
public class StatisticStateCounter<T> {

    private final SimTimeProvider simulation;

    private final HashMap<T, Double> statesTotalDuration;
    private final HashMap<T, Long> statesHistogram;

    private double lastUpdate;
    private T lastState;

    public StatisticStateCounter(final SimTimeProvider simulation) {
        this.simulation = simulation;
        this.statesTotalDuration = new HashMap<>();
        this.statesHistogram = new HashMap<>();
    }

    public void setState(final T newState) {
        if (lastState == null) {
            lastUpdate = simulation.getSimulationTime();
            lastState = newState;
            statesHistogram.put(newState, 1L);

        } else if (!newState.equals(lastState)) {
            final double duration = simulation.getSimulationTime() - lastUpdate;
            statesTotalDuration.compute(lastState, (state, totalDuration) -> totalDuration == null? duration : totalDuration + duration);
            statesHistogram.compute(newState, (state, occurCount) -> occurCount == null? 1 : occurCount +1 );
            lastState = newState;
        }
    }

    public T getState() {
        return lastState;
    }

    //TODO gettery na pocenotsti
}
