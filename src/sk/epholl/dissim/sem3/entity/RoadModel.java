package sk.epholl.dissim.sem3.entity;

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

        addRoad(Place.Enterance, Place.MainLot, Vehicle.State.MoveToOfficeLot, 13.32, 7.0);
        addRoad(Place.MainLot, Place.ParkingLot1, Vehicle.State.MoveToLot1,77.04, 0.0);
        addRoad(Place.ParkingLot2, Place.MainLot, Vehicle.State.ReturnToOfficeLot,102.96,0);
        addRoad(Place.MainLot, Place.Enterance, Vehicle.State.LeaveFromOfficeLot,13.32, 7.0);
    }

    private void addRoad(final Place start, final Place finish, final Vehicle.State vehicleState, final double duration, final double additionalDelay) {
        roads.get(start).put(finish, new Road(start, finish, vehicleState, duration, additionalDelay));
    }
}
