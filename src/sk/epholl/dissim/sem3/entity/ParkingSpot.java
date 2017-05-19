package sk.epholl.dissim.sem3.entity;

import sk.epholl.dissim.sem3.simulation.Rst;

/**
 * Created by Tomáš on 12.05.2017.
 */
public class ParkingSpot {

    public enum State {
        Free, Reserved, Occupied
    }

    private String name;
    private State state;
    private Vehicle vehicle;

    public ParkingSpot(String name) {
        this.name = name;
        this.state = State.Free;
        this.vehicle = null;
    }

    public State getState() {
        return state;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void clear() {
        state = State.Free;
        vehicle = null;
    }

    @Override
    public String toString() {
        return name;
    }

    public Rst.ParkingSpotState getSpotState() {
        Rst.ParkingSpotState state = new Rst.ParkingSpotState();
        state.name = name;
        state.state = this.state.toString();
        state.vehicle = this.vehicle == null? " - " : this.vehicle.getName();
        return state;
    }
}
