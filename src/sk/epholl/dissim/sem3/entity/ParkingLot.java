package sk.epholl.dissim.sem3.entity;

import sk.epholl.dissim.entity.*;

import java.util.List;

/**
 * Created by Tomáš on 12.05.2017.
 */
public class ParkingLot {

    private int capacity;

    private int freeSpots;

    private Vehicle[] spots;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        this.spots = new Vehicle[capacity];
        this.freeSpots = capacity;
    }

    
}
