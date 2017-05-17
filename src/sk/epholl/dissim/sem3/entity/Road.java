package sk.epholl.dissim.sem3.entity;

import sk.epholl.dissim.util.Pair;

import java.util.LinkedList;

/**
 * Created by Tomáš on 05.05.2017.
 */
public class Road {

    private final Place start;
    private final Place finish;

    private final Vehicle.State vehicleState;

    private final double travelDuration;
    private final double additionalDelay;

    public Road(final Place start, final Place finish, Vehicle.State vehicleState, final double travelDuration, final double additionalDelay) {
        this.start = start;
        this.finish = finish;
        this.vehicleState = vehicleState;
        this.travelDuration = travelDuration;
        this.additionalDelay = additionalDelay;
    }

    public double getTotalDuration() {
        return travelDuration + additionalDelay;
    }

    public Vehicle.State getVehicleState() {
        return vehicleState;
    }

    @Override
    public String toString() {
        return "Road " + start + " to " + finish;
    }
}
