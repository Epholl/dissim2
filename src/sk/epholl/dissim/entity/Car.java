package sk.epholl.dissim.entity;

/**
 * Created by Tomáš on 23.03.2017.
 */
public class Car {

    public static long carIndex = 0;

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

    public Car(double simulationEntryTime) {
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

    @Override
    public String toString() {
        return "Car " + index;
    }
}
