package sk.epholl.dissim.util;

import OSPABA.Simulation;

import java.util.HashMap;

/**
 * Created by Tomáš on 05.05.2017.
 */
public class StatisticStateCounter {

    private final Simulation simulation;

    private final HashMap<String, Double> statesTotalDuration;
    private final HashMap<String, Long> statesHistogram;

    private double lastUpdate;
    private String lastState;

    public StatisticStateCounter(final Simulation simulation) {
        this.simulation = simulation;
        this.statesTotalDuration = new HashMap<>();
        this.statesHistogram = new HashMap<>();
    }

    public void setState(final String newState) {
        if (lastState == null) {
            lastUpdate = simulation.currentTime();
            lastState = newState;
            statesHistogram.put(newState, 1L);

        } else if (!newState.equals(lastState)) {
            final double duration = simulation.currentTime() - lastUpdate;
            statesTotalDuration.compute(lastState, (state, totalDuration) -> totalDuration == null? duration : totalDuration + duration);
            statesHistogram.compute(newState, (state, occurCount) -> occurCount == null? 1 : occurCount +1 );
            lastState = newState;
        }
    }

    public String getState() {
        return lastState;
    }

    //TODO gettery na pocenotsti
}
