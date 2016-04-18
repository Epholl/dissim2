package sk.epholl.dissim.entity;

import sk.epholl.dissim.core.SimulationCore;
import sk.epholl.dissim.event.UnloadingFinishedEvent;
import sk.epholl.dissim.util.Utils;

/**
 * Created by Tomáš on 13.04.2016.
 */
public class Unloader extends SimulationComponent {

    private double unloadingSpeed;
    private double cargoAmount;

    private boolean isWorking = false;

    private double targetCargoAmount;

    public Unloader(SimulationCore core, String name, double unloadingSpeed, double targetCargoAmount) {
        super(core, name);
        this.unloadingSpeed = unloadingSpeed;
        this.targetCargoAmount = targetCargoAmount;
        this.cargoAmount = 0d;
    }

    @Override
    public void accept(Vehicle vehicle) {
        arrivalsCount++;
        saveQueueState();
        if (entryQueue.isEmpty() && !isWorking) {
            isWorking = true;
            vehicle.setState(Vehicle.STATE_UNLOADING);
            startUnloading(vehicle);
        } else {
            vehicle.setState(Vehicle.STATE_WAITING + getName());
            vehicle.setWaitingStartedTime(simulationCore.getSimulationTime());
            entryQueue.addLast(vehicle);
        }
    }

    @Override
    public void finished(Vehicle vehicle) {
        if (!entryQueue.isEmpty()) {
            saveQueueState();
            Vehicle next = entryQueue.pollFirst();
            double waitingTime = simulationCore.getSimulationTime() - next.getWaitingStartedTime();
            waitingTimeSum += waitingTime;
            next.setWaitingStartedTime(0d);
            next.setState(Vehicle.STATE_UNLOADING);
            startUnloading(next);
        } else {
            isWorking = false;
        }
        onFinishedListener.onVehicleFinished(vehicle);
    }

    public State getState() {
        return new State(cargoAmount, targetCargoAmount, getWaitingTimeSum(), entryQueue.size(), getAverageQueueLength(), getAverageWaitTime());
    }

    public double getCurrentCargoAmount() {
        return cargoAmount;
    }

    public boolean allCargoTransferred() {
        return cargoAmount >= targetCargoAmount;
    }

    public void acceptCargo(double cargoAmount) {
        this.cargoAmount += cargoAmount;
    }

    private void startUnloading(Vehicle vehicle) {
        double currentTime = simulationCore.getSimulationTime();
        double transferredCargo = vehicle.getCurrentLoad();
        double unloadingTimeHours = transferredCargo / unloadingSpeed;
        double unloadingTimeSeconds = Utils.hoursToSeconds(unloadingTimeHours);

        simulationCore.addEvent(new UnloadingFinishedEvent(currentTime + unloadingTimeSeconds, vehicle, this));
    }

    public static class State {
        public double cargoAmount;
        public double targetAmount;
        public double waitingTimeSum;
        public int queueLength;
        public double averageQueueLength;
        public double averageWaitingTime;

        public State(double cargoAmount, double targetAmount, double waitingTimeSum, int queueLength, double averageQueueLength, double averageWaitingTime) {
            this.cargoAmount = cargoAmount;
            this.targetAmount = targetAmount;
            this.waitingTimeSum = waitingTimeSum;
            this.queueLength = queueLength;
            this.averageQueueLength = averageQueueLength;
            this.averageWaitingTime = averageWaitingTime;
        }
    }
}
