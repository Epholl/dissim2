package sk.epholl.dissim.util;

import sk.epholl.dissim.util.SimTimeProvider;

import java.util.HashMap;

/**
 * Created by Tomáš on 19.05.2017.
 */
public class StatisticNumberCounter {

    private SimTimeProvider simulation;

    private HashMap<Integer, Double> statesDurations;

    private double lastUpdate;
    private int lastState;

    public StatisticNumberCounter(final SimTimeProvider simulation) {
        this.simulation = simulation;
        statesDurations = new HashMap<>();
    }

    public void setCurrentState(final int newState) {

        final double duration = simulation.getSimulationTime() - lastUpdate;
        statesDurations.compute(lastState, (state, totalDuration) -> totalDuration == null? duration : totalDuration + duration);
        lastState = newState;
        lastUpdate = simulation.getSimulationTime();
    }

    public void clear() {
        statesDurations.clear();
        lastState = 0;
        lastState = 0;
    }

    public double getAverage() {
        double sum = 0;
        for (Integer i: statesDurations.keySet()) {
            sum += i * statesDurations.get(i);
        }
        return sum;
    }
}
