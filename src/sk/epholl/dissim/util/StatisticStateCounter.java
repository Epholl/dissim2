package sk.epholl.dissim.util;

import java.util.HashMap;

/**
 * Created by Tomáš on 05.05.2017.
 */
public class StatisticStateCounter<T> {

    private final SimTimeProvider simulation;

    private final HashMap<T, Double> statesTotalDuration;
    private final HashMap<T, Integer> statesHistogram;

    private double lastUpdate;
    private T lastState;

    public StatisticStateCounter(final SimTimeProvider simulation) {
        this.simulation = simulation;
        this.statesTotalDuration = new HashMap<>();
        this.statesHistogram = new HashMap<>();
    }

    public void setCurrentState(final T newState) {
        if (lastState == null) {
            lastState = newState;
            statesHistogram.put(newState, 1);
            lastUpdate = simulation.getSimulationTime();

        } else {
            final double duration = simulation.getSimulationTime() - lastUpdate;
            statesTotalDuration.compute(lastState, (state, totalDuration) -> totalDuration == null? duration : totalDuration + duration);
            statesHistogram.compute(newState, (state, occurCount) -> occurCount == null? 1 : occurCount +1 );
            lastState = newState;
            lastUpdate = simulation.getSimulationTime();
        }
    }

    public void clear() {
        statesTotalDuration.clear();
        statesHistogram.clear();
        lastUpdate = 0;
        lastState = null;
    }

    public T getCurrentState() {
        return lastState;
    }

    public int getStateAmount(T state) {
        Integer count = statesHistogram.get(state);
        return count == null? 0 : count;
    }

    public int getTotalStateAmounts() {
        int states = 0;
        for (Integer count: statesHistogram.values()) {
            states += count;
        }
        return states;
    }

    public double getStateDurationCoeficient(T state) {
        double duration = statesTotalDuration.get(state) != null? statesTotalDuration.get(state) : 0.0;
        if (lastState == state) {
            duration += simulation.getSimulationTime() - lastUpdate;
        }
        return duration / simulation.getSimulationTime();
    }

    public double getNotStateDurationCoeficient(T state) {
        return 1 - getStateDurationCoeficient(state);
    }
}
