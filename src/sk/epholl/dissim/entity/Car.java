package sk.epholl.dissim.entity;

import sk.epholl.dissim.carshop.CarShopSimulationCore;

/**
 * Created by Tomáš on 23.03.2017.
 */
public class Car {

    public enum State {
        WaitingForOrder("Waiting to enter"),
        TakingOrder("Taking order"),
        Acquiring("Acquiring from customer"),
        MovingToWorkshop("Transferring to workshop"),
        WaitingForRepair("Waiting to be repaired"),
        Repairing("Repairing"),
        WaitingForReturnFromRepair("Waiting to be returned from workshop"),
        MovingFromWorkshop("Transferring from workshop"),
        ReturningToCustomer("Returning to customer"),
        Finished("Finished");

        private String name;

        State(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    public static long carIndex = 0;

    private final CarShopSimulationCore simulation;
    private final long index;

    private double timeEnteredSimulation;
    private double timeOrderStartedTaking;
    private double timeOrderTaken;
    private double timeCarAcquired;
    private double timeCarTransferredToGarage;
    private double timeCarStartedRepairing;
    private double timeCarRepaired;
    private double timeCarStartedTransferringFromGarage;
    private double timeCarTransferredFromGarage;
    private double timeCarReturnedToCustomer;

    private double orderTakingDuration;
    private double carAcquisitionDuration;
    private double transferToGarageDuration;
    private double repairDuration;
    private double transferFromGarageDuration;
    private double returnToCustomerDuration;

    public Car(CarShopSimulationCore simulation, double simulationEntryTime) {
        this.simulation = simulation;
        timeEnteredSimulation = simulationEntryTime;
        index = carIndex++;
    }

    public void setTimeOrderStartedTaking(double timeOrderStartedTaking) {
        this.timeOrderStartedTaking = timeOrderStartedTaking;
    }

    public void setTimeOrderTaken(double timeOrderTaken) {
        this.timeOrderTaken = timeOrderTaken;
    }

    public void setTimeCarAcquired(double timeCarAcquired) {
        this.timeCarAcquired = timeCarAcquired;
    }

    public void setTimeCarTransferredToGarage(double timeCarTransferredToGarage) {
        this.timeCarTransferredToGarage = timeCarTransferredToGarage;
    }

    public void setTimeCarStartedRepairing(double timeCarStartedRepairing) {
        this.timeCarStartedRepairing = timeCarStartedRepairing;
    }

    public void setTimeCarRepaired(double timeCarRepaired) {
        this.timeCarRepaired = timeCarRepaired;
    }

    public void setTimeCarStartedTransferringFromGarage(double timeCarStartedTransferringFromGarage) {
        this.timeCarStartedTransferringFromGarage = timeCarStartedTransferringFromGarage;
    }

    public void setTimeCarTransferredFromGarage(double timeCarTransferredFromGarage) {
        this.timeCarTransferredFromGarage = timeCarTransferredFromGarage;
    }

    public void setTimeCarReturnedToCustomer(double timeCarReturnedToCustomer) {
        this.timeCarReturnedToCustomer = timeCarReturnedToCustomer;
    }


    public void setOrderTakingDuration(double orderTakingDuration) {
        this.orderTakingDuration = orderTakingDuration;
    }

    public void setCarAcquisitionDuration(double carAcquisitionDuration) {
        this.carAcquisitionDuration = carAcquisitionDuration;
    }

    public void setTransferToGarageDuration(double transferToGarageDuration) {
        this.transferToGarageDuration = transferToGarageDuration;
    }

    public void setRepairDuration(double repairDuration) {
        this.repairDuration = repairDuration;
    }

    public void setTransferFromGarageDuration(double transferFromGarageDuration) {
        this.transferFromGarageDuration = transferFromGarageDuration;
    }

    public void setReturnToCustomerDuration(double returnToCustomerDuration) {
        this.returnToCustomerDuration = returnToCustomerDuration;
    }

    public long getId() {
        return index;
    }

    public double getEntryTime() {
        return timeEnteredSimulation;
    }

    public double getExitTime() {
        return timeCarReturnedToCustomer;
    }

    public double getTimeSpentInSystem() {
        return getExitTime() - getEntryTime();
    }

    public double getTimeSpentInSystemWithOvertimes() {
        double sum = timeOrderStartedTaking - timeEnteredSimulation;
        sum += orderTakingDuration;
        sum += carAcquisitionDuration;
        sum += transferToGarageDuration;
        sum += timeCarStartedRepairing - timeCarTransferredToGarage;
        sum += repairDuration;
        sum += timeCarStartedTransferringFromGarage - timeCarRepaired;
        sum += transferFromGarageDuration;
        sum += returnToCustomerDuration;
        return sum;
    }

    public double getTimeFromAcquisitionEndToReturn() {
        return timeCarReturnedToCustomer - timeCarTransferredToGarage;
    }

    public State getState() {
        final double simTime = simulation.getSimulationTime();
        if (timeOrderStartedTaking <= 0.001d) {
            return State.WaitingForOrder;
        } else if (timeOrderTaken >= simTime) {
            return State.TakingOrder;
        } else if (timeCarAcquired >= simTime) {
            return State.Acquiring;
        } else if (timeCarTransferredToGarage >= simTime) {
            return State.MovingToWorkshop;
        } else if (timeCarStartedRepairing <= 0.001d) {
            return State.WaitingForRepair;
        } else if (timeCarRepaired >= simTime) {
            return State.Repairing;
        } else if (timeCarStartedTransferringFromGarage <= 0.001d) {
            return State.WaitingForReturnFromRepair;
        } else if (timeCarTransferredFromGarage >= simTime) {
            return State.MovingFromWorkshop;
        } else if (timeCarReturnedToCustomer >= simTime) {
            return State.ReturningToCustomer;
        } else if (timeCarReturnedToCustomer <= simTime) {
            return State.Finished;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Car " + index + ": " + getState().toString();
    }
}
