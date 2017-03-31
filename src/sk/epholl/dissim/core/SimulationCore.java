package sk.epholl.dissim.core;

import sk.epholl.dissim.event.PauseEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Tomáš on 26.03.2016.
 */
public abstract class SimulationCore<T extends SimulationParameters, S, U> {

    public interface ResultListener<S, U> {
        void onReplicationFinished(S result);
        void onContinuousUpdate(U state);
    }

    protected final T simulationParameters;

    private long simulationNextEventId = 0;

    private double simulationTime;
    private boolean stopped;
    private boolean paused;

    private volatile double simulationSpeed = 20d;

    private PauseEvent pauseEvent;

    private List<ResultListener<S, U>> listeners;

    private PriorityQueue<Event> events;

    public SimulationCore(T simulationParameters) {
        this.simulationParameters = simulationParameters;
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
        while (!paused && !stopped && !events.isEmpty()) {
            Event current = events.poll();
            simulationTime = current.getOccurTime();
            if (simulationEndCondition()) {
                break;
            }
            current.onOccur();
        }
;        if (paused) {
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

    public void addListener(ResultListener<S, U> listener) {
        listeners.add(listener);
    }

    public void addEvent(Event added) {
        added.setSimulationEnrollId(simulationNextEventId++);
        added.setEnrollTime(getSimulationTime());
        events.add(added);
    }

    protected abstract boolean simulationEndCondition();
    protected abstract void singleIteration();

    public abstract S getResults();

    public abstract U getState();

    public void publishResults() {
        for (ResultListener<S, U> listener : listeners) {
            listener.onReplicationFinished(getResults());
        }
    }

    public void publishContinuousStateResults() {
        for (ResultListener<S, U> listener : listeners) {
            listener.onContinuousUpdate(getState());
        }
    }
}
