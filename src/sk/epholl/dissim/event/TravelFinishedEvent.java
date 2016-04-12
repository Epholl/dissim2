package sk.epholl.dissim.event;

import sk.epholl.dissim.core.Event;
import sk.epholl.dissim.entity.SimulationComponent;
import sk.epholl.dissim.entity.Vehicle;

/**
 * Created by Tomáš on 12.04.2016.
 */
public class TravelFinishedEvent extends Event {

    private Vehicle vehicle;
    private SimulationComponent component;

    public TravelFinishedEvent(double time, Vehicle vehicle, SimulationComponent component) {
        super(time);
        this.vehicle = vehicle;
        this.component = component;
    }

    @Override
    public void onOccur() {
        component.finished(vehicle);
    }
}
