package sk.epholl.dissim.sem3.entity;

import sk.epholl.dissim.sem3.simulation.MySimulation;
import sk.epholl.dissim.sem3.simulation.Rst;
import sk.epholl.dissim.util.StatisticStateCounter;

import static sk.epholl.dissim.sem3.entity.Worker2.State.Idle;

/**
 * Created by Tomáš on 14.05.2017.
 */
public class Worker2 {

    public enum State {
        Idle, Repairing, WaitingToReturnCar
    }

    private MySimulation sim;

    private StatisticStateCounter<State> stateCounter;

    private Vehicle vehicle;

    private final int id;

    public Worker2(final MySimulation sim, final int id) {
        this.id = id;
        this.sim = sim;
        this.stateCounter = new StatisticStateCounter<>(sim);
        this.stateCounter.setCurrentState(Idle);
    }

    public void setState(State newState) {
        stateCounter.setCurrentState(newState);
    }

    public Rst.WorkerState getWorkerState() {
        Rst.WorkerState state = new Rst.WorkerState();
        state.name = "Worker2 " + id;
        state.state = stateCounter.getCurrentState().toString();
        state.vehicle = vehicle == null? " - " : vehicle.getName();
        return state;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        return "Worker2 " + id;
    }
}
