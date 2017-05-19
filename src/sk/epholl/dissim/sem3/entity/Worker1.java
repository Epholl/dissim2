package sk.epholl.dissim.sem3.entity;

import sk.epholl.dissim.sem3.simulation.MySimulation;
import sk.epholl.dissim.sem3.simulation.Rst;
import sk.epholl.dissim.util.StatisticStateCounter;

/**
 * Created by Tomáš on 12.05.2017.
 */
public class Worker1 {

    public enum State {
        Idle, TakingOrder, ReturningVehicle
    }

    private MySimulation sim;

    private StatisticStateCounter<State> stateCounter;

    private Vehicle vehicle;

    private final int id;

    public Worker1(final MySimulation sim, final int id) {
        this.id = id;
        this.sim = sim;
        this.stateCounter = new StatisticStateCounter<>(sim);
        this.stateCounter.setCurrentState(State.Idle);
    }

    public void setState(State newState) {
        stateCounter.setCurrentState(newState);
    }

    public Rst.WorkerState getWorkerState() {
        Rst.WorkerState state = new Rst.WorkerState();
        state.name = "Worker1 " + id;
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

    public double getWorkLoadCoeficient() {
        return stateCounter.getNotStateCoeficient(State.Idle);
    }

    @Override
    public String toString() {
        return "Worker1 " + id;
    }
}
