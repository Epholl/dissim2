package sk.epholl.dissim.entity;

import sk.epholl.dissim.core.SimulationCore;
import sk.epholl.dissim.event.LoadingFinishedEvent;
import sk.epholl.dissim.util.Utils;

/**
 * Created by Tomáš on 13.04.2016.
 */
public class Loader extends SimulationComponent {

    private double loadingSpeed;
    private double cargoAmount;

    private boolean isWorking = false;

    public Loader(SimulationCore core, String name, double loadingSpeed, double cargoAmount) {
        super(core, name);
        this.loadingSpeed = loadingSpeed;
        this.cargoAmount = cargoAmount;
    }

    @Override
    public void accept(Vehicle vehicle) {
        arrivalsCount++;
        saveQueueState();
        if (!isWorking) {
            isWorking = true;
            vehicle.setState(Vehicle.STATE_LOADING);
            startLoading(vehicle);
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
            next.setState(Vehicle.STATE_LOADING);
            startLoading(next);
        } else {
            isWorking = false;
        }
        onFinishedListener.onVehicleFinished(vehicle);
    }

    public State getState() {
        return new State(cargoAmount, entryQueue.size(), getWaitingTimeSum(), getAverageQueueLength(), getAverageWaitTime());
    }

    public void substractCargo(double cargoAmount) {
        this.cargoAmount -= cargoAmount;
    }

    private void startLoading(Vehicle vehicle) {
        double currentTime = simulationCore.getSimulationTime();
        double transferredCargo = Math.min(vehicle.getCapacity(), cargoAmount);
        double loadingTimeHours = transferredCargo / loadingSpeed;
        double loadingTimeSeconds = Utils.hoursToSeconds(loadingTimeHours);

        simulationCore.addEvent(new LoadingFinishedEvent(currentTime + loadingTimeSeconds, vehicle, this, transferredCargo));
    }

    public static class State {
        public double cargoAmount;
        public int queueLength;
        public double waitingTimeSum;
        public double averageQueueLength;
        public double averageWaitingTime;

        public State(double cargoAmount, int queueLength, double waitingTimeSum,  double averageQueueLength, double averageWaitingTime) {
            this.cargoAmount = cargoAmount;
            this.queueLength = queueLength;
            this.waitingTimeSum = waitingTimeSum;
            this.averageQueueLength = averageQueueLength;
            this.averageWaitingTime = averageWaitingTime;
        }
    }
}
