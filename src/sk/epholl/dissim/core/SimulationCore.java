package sk.epholl.dissim.core;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Tomáš on 26.03.2016.
 */
public abstract class SimulationCore<T> {

    public interface ResultListener<T> {
        T onReplicationFinished();
        T onContinuousUpdate();
    }

    private long simulationNextEventId = 0;

    private double simulationTime;
    private boolean stopped;
    private boolean paused;

    private List<ResultListener<T>> listeners;

    private PriorityQueue<Event> events;

    public SimulationCore() {
        simulationTime = 0d;
        stopped = true;
        paused = false;
        listeners = new LinkedList<>();
        events = new PriorityQueue<>();
    }

    public void start() {
        stopped = false;
        resume();
    }

    public void resume() {
        while (!paused && !stopped && !simulationEndCondition() && !events.isEmpty()) {
            Event current = events.poll();
            simulationTime = current.getOccurTime();
            current.onOccur();
        }
    }

    public double getSimulationTime() {
        return simulationTime;
    }

    public void addListener(ResultListener<T> listener) {
        listeners.add(listener);
    }

    public void addEvent(Event added) {
        added.setSimulationEnrollId(simulationNextEventId++);
        events.add(added);
    }

    protected abstract boolean simulationEndCondition();
}
