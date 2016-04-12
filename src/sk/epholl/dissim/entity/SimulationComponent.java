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

    protected LinkedList<Vehicle> entryQueue = new LinkedList<>();

    protected EventFinishedListener onFinishedListener;

    public SimulationComponent(SimulationCore core) {
        this.simulationCore = core;
    }

    public void setOnFinishedListener (EventFinishedListener listener) {
        this.onFinishedListener = listener;
    }

    public abstract void accept(Vehicle vehicle);

    public abstract void finished(Vehicle vehicle);
}
