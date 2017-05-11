package sk.epholl.dissim.sem2.entity;

import sk.epholl.dissim.sem2.core.SimulationCore;

import java.util.LinkedList;

/**
 * Created by Tomáš on 12.04.2016.
 */
public abstract class SimulationComponent<T> implements Cloneable {

    public interface EventFinishedListener<T> {
        void onFinished(T vehicle);
    }

    protected SimulationCore simulation;

    protected String name;

    protected LinkedList<T> entryQueue = new LinkedList<>();

    protected EventFinishedListener onFinishedListener;

    protected double waitingTimeSum = 0d;
    protected long arrivalsCount = 0;

    protected double[] queueLengths = new double[10];
    protected double lastQueueLengthUpdate = 0d;

    public SimulationComponent(SimulationCore sim, String name) {
        this.simulation = sim;
        this.name = name;
    }

    public void setOnFinishedListener (EventFinishedListener listener) {
        this.onFinishedListener = listener;
    }

    public int getCurrentQueueLength() {
        return entryQueue.size();
    }

    public double getAverageWaitTime() {
        return waitingTimeSum / arrivalsCount;
    }

    public double getWaitingTimeSum() {
        return waitingTimeSum;
    }

    public double getAverageQueueLength() {
        double totalTime = simulation.getSimulationTime();
        double sum = 0d;
        for (int i = 0; i < queueLengths.length; i++) {
            sum += queueLengths[i] * i;
        }

        return sum / totalTime;
    }

    public void saveQueueState() {
        queueLengths[entryQueue.size()] += simulation.getSimulationTime() - lastQueueLengthUpdate;
        lastQueueLengthUpdate = simulation.getSimulationTime();
    }

    public String getName() {
        return name;
    }

    public abstract void accept(T vehicle);

    public abstract void finished(T vehicle);
}
