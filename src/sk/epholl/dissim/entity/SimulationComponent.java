package sk.epholl.dissim.entity;

import sk.epholl.dissim.core.SimulationCore;

import java.util.LinkedList;

/**
 * Created by Tomáš on 12.04.2016.
 */
public abstract class SimulationComponent implements Cloneable {

    public interface EventFinishedListener {
        void onVehicleFinished(Vehicle vehicle);
    }

    protected SimulationCore simulationCore;

    protected String name;

    protected LinkedList<Vehicle> entryQueue = new LinkedList<>();

    protected EventFinishedListener onFinishedListener;

    protected double waitingTimeSum = 0d;
    protected long arrivalsCount = 0;

    protected double[] queueLengths = new double[10];
    protected double lastQueueLengthUpdate = 0d;

    public SimulationComponent(SimulationCore core, String name) {
        this.simulationCore = core;
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
        double totalTime = simulationCore.getSimulationTime();
        double sum = 0d;
        for (int i = 0; i < queueLengths.length; i++) {
            sum += queueLengths[i] * i;
        }

        return sum / totalTime;
    }

    public void saveQueueState() {
        queueLengths[entryQueue.size()] += simulationCore.getSimulationTime() - lastQueueLengthUpdate;
        lastQueueLengthUpdate = simulationCore.getSimulationTime();
    }

    public String getName() {
        return name;
    }

    public abstract void accept(Vehicle vehicle);

    public abstract void finished(Vehicle vehicle);
}
