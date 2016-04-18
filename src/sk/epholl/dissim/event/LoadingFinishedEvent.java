package sk.epholl.dissim.event;

import sk.epholl.dissim.core.Event;
import sk.epholl.dissim.entity.Loader;
import sk.epholl.dissim.entity.Vehicle;

/**
 * Created by Tomáš on 13.04.2016.
 */
public class LoadingFinishedEvent extends Event {

    private Vehicle vehicle;
    private Loader loader;

    private double cargoAmount;

    public LoadingFinishedEvent(double occurTime, Vehicle vehicle, Loader loader, double cargoAmount) {
        super(occurTime);
        this.vehicle = vehicle;
        this.loader = loader;
        this.cargoAmount = cargoAmount;
    }


    @Override
    public void onOccur() {
        vehicle.load(cargoAmount);
        loader.substractCargo(cargoAmount);
        loader.finished(vehicle);
    }
}
