package sk.epholl.dissim.sem3.entity;

import OSPABA.Entity;
import sk.epholl.dissim.sem3.simulation.MySimulation;
import sun.applet.Main;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tomáš on 05.05.2017.
 */
public class RoadModel {

    private Map<Place, Map<Place, Road>> roads;

    public RoadModel() {

        init();
    }

    public Road getRoad(final Place start, final Place finish) {
        return roads.get(start).get(finish);
    }

    private void init() {
        roads = new HashMap<>(new HashMap<>());
        for (Place place: Place.values()) {
            roads.put(place, new HashMap<>());
        }

        addRoad(Place.Enterance, Place.MainLot, 13.32, 7.0);
        addRoad(Place.MainLot, Place.ParkingLot1, 77.04, 0.0);
        addRoad(Place.ParkingLot1, Place.RepairShop, 0, 0);
        addRoad(Place.RepairShop, Place.ParkingLot2, 0, 0);
        addRoad(Place.ParkingLot2, Place.MainLot, 102.96,0);
        addRoad(Place.MainLot, Place.Enterance, 13.32, 7.0);
    }

    private void addRoad(final Place start, final Place finish, final double duration, final double additionalDelay) {
        roads.get(start).put(finish, new Road(start, finish, duration, additionalDelay));
    }
}
