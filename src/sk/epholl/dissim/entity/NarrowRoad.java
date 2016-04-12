package sk.epholl.dissim.entity;

import sk.epholl.dissim.core.SimulationCore;
import sk.epholl.dissim.event.TravelFinishedEvent;

import java.util.LinkedList;

/**
 * Created by Tomáš on 10.04.2016.
 */
public class NarrowRoad extends SimulationComponent {

    private double length;

    private double lastArrivalTime;

    private LinkedList<Vehicle> passingVehicles = new LinkedList<>();

    public NarrowRoad(SimulationCore core, double length) {
        super(core);
        this.length = length;
    }

    @Override
    public void accept(Vehicle vehicle) {

        double currentTime = simulationCore.getSimulationTime();
        double travelDurationInHours = length / vehicle.getSpeed();
        double travelDurationInSeconds = travelDurationInHours * 3600;
        double arriveTime = currentTime + travelDurationInSeconds;

        if (passingVehicles.isEmpty()) {
            passingVehicles.add(vehicle);

            simulationCore.addEvent(new TravelFinishedEvent(arriveTime, vehicle, this));
            lastArrivalTime = arriveTime;
        } else {
            passingVehicles.addLast(vehicle);

            double realArrivalTime = Math.max(arriveTime, lastArrivalTime);
            simulationCore.addEvent(new TravelFinishedEvent(realArrivalTime, vehicle, this));
            lastArrivalTime = realArrivalTime;
        }
    }

    @Override
    public void finished(Vehicle vehicle) {
        passingVehicles.remove(vehicle);
        onFinishedListener.onVehicleFinished(vehicle);
    }
}
