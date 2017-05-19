package sk.epholl.dissim.sem3.entity;

import OSPABA.Entity;
import sk.epholl.dissim.sem3.simulation.MySimulation;
import sk.epholl.dissim.sem3.simulation.Rst;
import sk.epholl.dissim.util.Pair;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tomáš on 12.05.2017.
 */
public class Vehicle extends Entity {

    public enum State {
        EnterSystem,
        MoveToOfficeLot,
        WaitForOrder,
        CancelOrder,
        ShopClosed,
        LeaveFromOfficeLot,
        StartTakingOrder,
        TakeOrderAndRetrieve,
        MoveToLot1,
        WaitForRepair,
        Repair,
        WaitingForLot2Spot,
        WaitingOnLot2,
        LeaveFromLot2,
        ReturnToOfficeLot,
        RetrieveCar,
        LeaveSystem
    }

    private long id;

    private LinkedList<Pair<Double, State>> history;

    private Place currentPlace;
    private Road currentRoad;

    private ParkingSpot assignedParkingSpot;

    private Worker1 worker1;
    private Worker2 worker2;

    private State currentState;
    private double currentStateFinishTime;

    private int repairDuratioinInMinutes;

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

    public void setCurrentState(final State currentState) {
        this.currentState = currentState;
    }

    public void persistCurrentState() {
        if (currentState == null) {
            throw new AssertionError("Attempted to persist a null currentState");
        }
        history.add(new Pair<>(mySim().currentTime(), currentState));
    }

    public List<Pair<Double, State>> getHistory() {
        return Collections.unmodifiableList(history);
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

    public Worker1 getWorker1() {
        return worker1;
    }

    public void setWorker1(Worker1 worker1) {
        this.worker1 = worker1;
        if (worker1 != null) {
            worker1.setVehicle(this);
        }
    }

    public Worker2 getWorker2() {
        return worker2;
    }

    public void setWorker2(Worker2 worker2) {
        this.worker2 = worker2;
    }

    public boolean isRepaired() {
        return isStateFinished(State.Repair);
    }

    public boolean isOrderCancelled() {
        return isStateFinished(State.CancelOrder);
    }

    public boolean isShopClosed() {
        return isStateFinished(State.ShopClosed);
    }

    public boolean isStateFinished(State state) {
        for (Pair<Double, State> pastState: history) {
            if (pastState.second == state) {
                return true;
            }
        }
        return false;
    }

    public int getRepairDuratioinInMinutes() {
        return repairDuratioinInMinutes;
    }

    public void setRepairDuratioinInMinutes(int repairDuratioinInMinutes) {
        this.repairDuratioinInMinutes = repairDuratioinInMinutes;
    }

    public String getName() {
        return "Vehicle " + id;
    }

    @Override
    public String toString() {
        Pair<Double, State> lastState = history.getLast();
        String position = currentPlace == null? currentRoad.toString() : currentPlace.toString();
        String workers = (worker1 == null && worker2 == null)? "no workers" : worker1 != null? worker1.toString() : worker2.toString();
        String parkingSpot = assignedParkingSpot == null? "no parking spot" : assignedParkingSpot.getState() == ParkingSpot.State.Reserved? "Spot reserved" : "Parking on spot;";
        return "Vehicle " + id + ": " + position + ", " + lastState.second + ", " + lastState.first + ", " + workers + ", " + parkingSpot;
    }

    public Rst.VehicleState getVehicleState() {
        Rst.VehicleState state = new Rst.VehicleState();
        state.name = "Vehicle " + id;
        state.position = currentPlace == null? currentRoad.toString() : currentPlace.toString();
        state.state = "" + currentState;
        state.worker = worker1 != null? worker1.toString() : worker2 != null? worker2.toString() : " - ";
        state.timeStateStarted = lastEventTime();
        state.timeStateEnds = currentStateFinishTime;
        return state;
    }

    private double lastEventTime() {
        if (history.isEmpty()) {
            return mySim().currentTime();
        } else {
            return history.peekLast().first;
        }
    }
}
