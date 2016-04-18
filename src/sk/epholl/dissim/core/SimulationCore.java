package sk.epholl.dissim.core;

import sk.epholl.dissim.event.PauseEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Tomáš on 26.03.2016.
 */
public abstract class SimulationCore<T> {

    public interface ResultListener<T> {
        void onReplicationFinished(T result);
        void onContinuousUpdate(T result);
    }

    private long simulationNextEventId = 0;

    private double simulationTime;
    private boolean stopped;
    private boolean paused;

    private volatile double simulationSpeed = 20d;

    private PauseEvent pauseEvent;

    private List<ResultListener<T>> listeners;

    private PriorityQueue<Event> events;

    public SimulationCore() {
        simulationTime = 0d;
        stopped = true;
        paused = false;
        listeners = new LinkedList<>();
        events = new PriorityQueue<>();
        pauseEvent = new PauseEvent(0d, this);
    }

    public void setContinuousRun(boolean continousRun) {
        if (continousRun) {
            pauseEvent.setActive(true);
            addEvent(pauseEvent);
        } else {
            pauseEvent.setActive(false);
        }
    }

    public void setContinuousSpeed(double speedMultiplier) {
        simulationSpeed = speedMultiplier;
    }

    public void start() {
        stopped = false;
        resume();
    }

    public void stop() {
        stopped = true;
    }

    public void resume() {
        while (!paused && !stopped && !simulationEndCondition() && !events.isEmpty()) {
            Event current = events.poll();
            simulationTime = current.getOccurTime();
            current.onOccur();
        }
        if (paused) {
            return;
        } else {
            publishResults();
        }
    }

    public double getSimulationTime() {
        return simulationTime;
    }

    public double getSimulationSpeed() {
        return simulationSpeed;
    }

    public void addListener(ResultListener<T> listener) {
        listeners.add(listener);
    }

    public void addEvent(Event added) {
        added.setSimulationEnrollId(simulationNextEventId++);
        added.setEnrollTime(getSimulationTime());
        events.add(added);
    }

    protected abstract boolean simulationEndCondition();

    public abstract T getResults();

    public void publishResults() {
        for (ResultListener<T> listener : listeners) {
            listener.onReplicationFinished(getResults());
        }
    }

    public void publishContinuousStateResults() {
        for (ResultListener<T> listener : listeners) {
            listener.onContinuousUpdate(getResults());
        }
    }
}
