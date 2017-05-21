package sk.epholl.dissim.sem3.entity;


import sk.epholl.dissim.sem3.simulation.MySimulation;
import sk.epholl.dissim.sem3.simulation.Rst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tomáš on 12.05.2017.
 */
public class ParkingLot {

    private int capacity;

    private int freeSpots;

    private ParkingSpot[] spots;

    private HashMap<Vehicle, Integer> parkedVehicles;

    private String name;

    public ParkingLot(MySimulation simulation, int capacity, String name) {
        this.capacity = capacity;
        this.spots = new ParkingSpot[capacity];
        this.name = name;
        for (int i = 0; i < spots.length; i++) {
            spots[i] = new ParkingSpot(simulation, name);
        }
        this.freeSpots = capacity;
        this.parkedVehicles = new HashMap<>();
    }

    public FreeCapacity getFreeCapacity() {
        return new FreeCapacity(capacity, freeSpots);
    }

    public ParkingSpot reserve(final Vehicle vehicle) {
        final int freeIndex = getFreeSpot();
        freeSpots--;
        parkedVehicles.put(vehicle, freeIndex);
        final ParkingSpot spot = spots[freeIndex];
        spot.setState(ParkingSpot.State.Reserved);
        spot.setVehicle(vehicle);
        vehicle.setAssignedParkingSpot(spot);
        return spot;
    }

    public void free(final Vehicle vehicle) {
        final int index = parkedVehicles.get(vehicle);
        parkedVehicles.remove(vehicle);
        final ParkingSpot spot = spots[index];
        spot.setVehicle(null);
        spot.setState(ParkingSpot.State.Free);
        freeSpots++;
    }

    public void clear() {
        parkedVehicles.clear();
        freeSpots = capacity;
        for (ParkingSpot spot: spots) {
            spot.clear();
        }
    }

    public List<Rst.ParkingSpotState> getSpotsStatus() {
        ArrayList<Rst.ParkingSpotState> spots = new ArrayList<>();
        for (ParkingSpot spot: this.spots) {
            spots.add(spot.getSpotState());
        }
        return spots;
    }

    public int getCapacity() {
        return capacity;
    }

    private int getFreeSpot() {
        int i = 0;
        for (;i<spots.length; i++) {
            if (spots[i].getState() == ParkingSpot.State.Free) {
                return i;
            }
        }

        throw new AssertionError("getFreeSpot called when no free spaces present");
    }
}
