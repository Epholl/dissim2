package sk.epholl.dissim.sem3.entity;

import OSPABA.Entity;
import sk.epholl.dissim.sem3.simulation.MySimulation;
import sk.epholl.dissim.util.Pair;

import java.util.LinkedList;

/**
 * Created by Tomáš on 12.05.2017.
 */
public class Vehicle extends Entity {

    public enum State {
        EnterSystem,
        MoveToOfficeLot,
        CancelOrder,
        MoveFromOfficeLot,
        TakeOrder,
        RetrieveFromCustomer,
        MoveToLot1,
        MoveToRepair,
        Repair,
        MoveToLot2,
        ReturnToOfficeLot,
        RetrieveCar,
        LeaveSystem
    }

    private long id;

    private LinkedList<Pair<Double, State>> history;

    private Place currentPlace;
    private Road currentRoad;

    private ParkingSpot assignedParkingSpot;

    private State currentState;
    private double currentStateFinishTime;

    public Vehicle(final MySimulation simulation, final long id) {
        super(simulation);
        this.id = id;
        this.history = new LinkedList<>();
    }

    public void addFinsihedState(final State state) {
        history.add(new Pair<>(mySim().currentTime(), state));
    }

    public void addFinishedState(final State state, final double time) {
        history.add(new Pair<>(time, state));
    }

    public void setCurrentState(final State currentState, final double timeFinnished) {
        this.currentState = currentState;
        this.currentStateFinishTime = timeFinnished;
    }

    public void persistCurrentState() {
        if (currentState == null) {
            throw new AssertionError("Attempted to persist a null currentState");
        }
        history.add(new Pair<>(currentStateFinishTime, currentState));
    }

    public Place getCurrentPlace() {
        return currentPlace;
    }

    public void setCurrentPlace(Place currentPlace) {
        this.currentPlace = currentPlace;
        currentRoad = null;
    }

    public Road getCurrentRoad() {
        return currentRoad;
    }

    public void setCurrentRoad(Road currentRoad) {
        this.currentRoad = currentRoad;
        currentPlace = null;
    }

    public ParkingSpot getAssignedParkingSpot() {
        return assignedParkingSpot;
    }

    public void setAssignedParkingSpot(ParkingSpot assignedParkingSpot) {
        this.assignedParkingSpot = assignedParkingSpot;
    }

    @Override
    public String toString() {
        Pair<Double, State> lastState = history.getLast();
        String position = currentPlace == null? currentRoad.toString() : currentPlace.toString();
        return "Vehicle " + id + ": " + position + ", " + lastState.second + ", " + lastState.first;
    }
}
