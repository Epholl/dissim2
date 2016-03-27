package sk.epholl.dissim.core;

import java.util.PriorityQueue;

/**
 * Created by Tomáš on 26.03.2016.
 */
public abstract class SimulationCore {

    private double simulationTime;
    private boolean stopped;
    private boolean paused;

    private PriorityQueue<Event> events;

    public SimulationCore() {
        simulationTime = 0d;
        stopped = true;
        paused = false;
        events = new PriorityQueue<>();
    }

    public void start() {
        stopped = false;
        resume();
    }

    public void resume() {
        while (!paused && !stopped && !simulationEndCondition() && !events.isEmpty()) {
            Event current = events.
        }
    }

    public double getSimulationTime() {
        return simulationTime;
    }

    public void addEvent(Event added) {

    }

    protected abstract boolean simulationEndCondition();
}
