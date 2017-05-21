package sk.epholl.dissim.sem3.entity;

import sk.epholl.dissim.sem3.simulation.Rst;
import sk.epholl.dissim.util.SimTimeProvider;
import sk.epholl.dissim.util.StatisticStateCounter;

/**
 * Created by Tomáš on 12.05.2017.
 */
public class ParkingSpot {

    public enum State {
        Free, Reserved, Occupied
    }

    private String name;
    private StatisticStateCounter<State> state;
    private Vehicle vehicle;

    public ParkingSpot(SimTimeProvider sim, String name) {
        this.name = name;
        this.state = new StatisticStateCounter<>(sim);
        state.setCurrentState(State.Free);
        this.vehicle = null;
    }

    public State getState() {
        return state.getCurrentState();
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setState(State state) {
        this.state.setCurrentState(state);
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void clear() {
        state.clear();
        state.setCurrentState(State.Free);
        vehicle = null;
    }

    @Override
    public String toString() {
        return name;
    }

    public Rst.ParkingSpotState getSpotState() {
        Rst.ParkingSpotState state = new Rst.ParkingSpotState();
        state.name = name;
        state.state = getState().toString();
        state.vehicle = getVehicle() == null? " - " : getVehicle().getName();
        state.loadCoeficient = getWorkLoadCoeficient();
        return state;
    }

    public double getWorkLoadCoeficient() {
        double coeficient = state.getNotStateDurationCoeficient(State.Free);
        return Double.isNaN(coeficient)? 0.0 : coeficient;
    }
}
